from xml.dom import minidom
import sys
dom = minidom

filename = sys.argv[1]
administrative_level = sys.argv[2]
optional_data = sys.argv[3] if len(sys.argv) >= 4 else None
if optional_data != None:
	optional_data_file = open(optional_data, "r")
	tmp_optional_data = optional_data_file.read().splitlines()
	optional_data_file.close()
	optional_data = {}
	firstline = tmp_optional_data[0]
	optional_data_names = firstline.split("\t")
	tmp_optional_data = tmp_optional_data[1:]
	for line in tmp_optional_data:
		if line.strip() != "":
			line = line.replace(" ", "")
			(key, values) = line.split("\t", 1)
			optional_data[key.lower().strip()] = values.split("\t")

file = open(filename, "r")
doc = dom.parse(file)
file.close()

root = doc.firstChild

relations = {}
ways = {}
nodes = {}

class osmRelation:
	def __init__(self, node):
		self.tags = {}
		self.ways = []
		self.waypoints = []
		for subnode in node.childNodes:
			if isinstance(subnode, dom.Element):
				if subnode.tagName == "tag":
					self.tags[subnode.attributes["k"].value] = subnode.attributes["v"].value
				elif subnode.tagName == "member":
					mType = subnode.attributes["type"].value
					mRef = subnode.attributes["ref"].value
					self.ways.append(mRef)
		try:
			if "name" in self.tags:
				self.name = self.tags["name"].replace(" ", "_")
			else:
				self.name = None
		except UnicodeEncodeError:
			self.name = None
		if "type" in self.tags:
			self.type = self.tags["type"]
		else:
			self.type = None
		if "admin_level" in self.tags:
			self.admin_level = self.tags["admin_level"]
		else:
			self.admin_level = None
	
	def __str__(self):
		# return str({"tags": self.tags, "ways": self.ways})
		return str(self.waypoints)
	
	def __repr__(self):
		# return repr({"tags": self.tags, "ways": self.ways})
		return repr(self.waypoints)
	
	def refWays(self, ways):
		self.waypoints = []
		__ways = []
		for way in self.ways:
			#self.waypoints += ways[way].coords
			__ways.append(ways[way])
		currentWay = __ways[0]
		__ways = __ways[1:]
		while len(__ways) > 0:
			#print str(len(__ways))
			for __way in __ways:
				__match = currentWay.endMatches(__way)
				if __match != 0:
					if __match == -1:
						__way.reverse()
						__match = 1
					if __match == 1:
						self.waypointsAppend(currentWay)
						currentWay = __way
						__ways.remove(__way)
						#print "FOUND"
						break
			if __match == 0:
				#print "NOT FOUND"
				self.waypoints = []
				return False
				break
	def info(self):
		return "NAME: " + repr(self.name) + " / TYPE: " + repr(self.type) + " / LEVEL: " + repr(self.admin_level)
	
	def waypointsAppend(self, way):
		self.waypoints += way.coords
	
	def normalize(self, factx, facty, biasx, biasy):
		temp = self.waypoints
		self.waypoints = []
		for (lat, lon) in temp:
			self.waypoints.append( ( ((lat-biasx)*factx), ((lon-biasy)*facty) ) )
	def toATM(self, optionalData):
		tmp = str(len(self.waypoints)) + "\n"
		for (x, y) in self.waypoints:
			tmp += str(int(x)) + " " + str(int(y)) + " "
		tmp += "\n"
		tmp += self.name + " " + " ".join(optionalData) + "\n"
		return tmp

class osmWay:
	def __init__(self, node):
		self.nodes = []
		self.tags = {}
		self.coords = []
		self.id = node.attributes["id"].value
		for subnode in node.childNodes:
			if isinstance(subnode, dom.Element):
				if subnode.tagName == "tag":
					self.tags[subnode.attributes["k"].value] = subnode.attributes["v"].value
				elif subnode.tagName == "nd":
					mRef = subnode.attributes["ref"].value
					self.nodes.append(mRef)
	def refNodes(self, nodes):
		self.coords = []
		for node in self.nodes:
			self.coords.append(nodes[node].tuple())
	
	def __repr__(self):
		return repr(self.coords)
	
	def __str__(self):
		return str(self.coords)
	
	def reverse(self):
		self.coords.reverse()
	
	def firstCoordinate(self):
		return self.coords[0]
	
	def lastCoordinate(self):
		return self.coords[-1]
	
	def endMatches(self, way):
		return 1 if self.lastCoordinate() == way.firstCoordinate() else -1 if self.lastCoordinate() == way.lastCoordinate() else 0

class osmNode:
	def __init__(self, node):
		self.id = node.attributes["id"].value
		self.lat = node.attributes["lat"].value
		self.lon = node.attributes["lon"].value
	def tuple(self):
		return (float(self.lon), float(self.lat))

for node in root.childNodes:
	if isinstance(node, dom.Element):
		if node.tagName == "relation":
			relation = osmRelation(node)
			if relation.name != None and relation.type == 'boundary' and relation.admin_level == administrative_level:
				relations[relation.name] = relation
			else:
				try:
					print(("Y" if relation.name != None else "N") + ("Y" if relation.type == 'boundary' else "N") + ("Y" if relation.admin_level == administrative_level else "N"))
					print(relation.info())
					print(repr(administrative_level))
					print(repr(relation.admin_level))
				except UnicodeEncodeError:
					print(relation.name.encode(sys.stdout.encoding, errors='ignore'))
					#print(relation.type.encode(sys.stdout.encoding, errors='ignore'))
					#print(relation.admin_level.encode(sys.stdout.encoding, errors='ignore'))
		elif node.tagName == "way":
			way = osmWay(node)
			if "boundary" in way.tags:
				ways[way.id] = way
		elif node.tagName == "node":
			_node = osmNode(node)
			nodes[_node.id] = _node

for way in ways:
	ways[way].refNodes(nodes)
del nodes

del_relations = []
for relation in relations:
	relations[relation].refWays(ways)
	if len(relations[relation].waypoints) == 0:
		#print relations[relation].name
		del_relations.append(relation)
		#print("?")
for rel in del_relations:
	del relations[rel]
del ways

smallestx = None
largestx = None
smallesty = None
largesty = None

for _relation in relations:
	relation = relations[_relation]
	for (x, y) in relation.waypoints:
		if smallestx == None or x < smallestx:
			smallestx = x
		if largestx == None or x > largestx:
			largestx = x
		if smallesty == None or y < smallesty:
			smallesty = y
		if largesty == None or y > largesty:
			largesty = y

diffx = largestx - smallestx
diffy = largesty - smallesty

if optional_data != None:
	file = str(len(optional_data_names)) + "\n" + "\n".join(optional_data_names) + "\n" + str(len(relations)) + "\n" #"Name\nWeight\n" + str(len(relations)) + "\n"
else:
	file = str(2) + "\n" + "\n".join(["Name", "random weight"]) + "\n" + str(len(relations)) + "\n"

k = 200

map_width = 400000
radiant = 0.0125
num_atms = 250

for _rel in relations:
	relation = relations[_rel]
	fact = map_width / diffx
	relation.normalize(fact, fact, smallestx, smallesty)
	if optional_data != None:
		#print repr(optional_data) + repr(str(relation.name.lower().replace("\xf6", "oe")))
		weights = optional_data[relation.name.lower().replace("\xf6", "oe")]
		#print repr(weights)
	else:
		weights = [str(k)]
		k = (200/k) + 5*k
	file += relation.toATM(weights)
file += str(num_atms) + "\n" + str(int(map_width * radiant))
#file = bytes(file)

#print relations
# print ways

#print file #.encode('iso-8859-1')

outfilename = filename[:-4] + "@" + str(administrative_level) + ".txt"
outfile = open(outfilename, "w")
outfile.write(file)
outfile.close()