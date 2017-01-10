import sys
import random

f = open("test.svg", "r")
cont = f.read().splitlines()
f.close()

stadtteile = []
i = 0

for line in cont:
	if 'd="m ' in line:
		i += 1
		line = line.strip()
		chunks = line.split(" ")
		stadtteil = {}
		stadtteil["name"] = "Stadtteil_" + str(i)
		stadtteil["punkte"] = []
		oldx = 0
		oldy = 0
		x = 0
		y = 0
		for chunk in chunks:
			try:
				(x, y) = chunk.split(",")
				#print("x: " + x + ", y: " + y)
				(x, y) = (float(x), float(y))
				#print("x: " + str(x) + ", y: " + str(y))
				(x, y) = (int(x), int(y))
				x = x + oldx
				y = y + oldy
				#print("x: " + str(x) + ", y: " + str(y))
				stadtteil["punkte"].append(str(x) + " " + str(y))
			except Exception:
				i = i
				#print(chunk)
			oldx = x
			oldy = y
		# stadtteil["punkte"] = " ".join(stadtteil["punkte"])
		k = random.randint(10000, 50000)
		p = random.randint(2000, 10000)
		stadtteil["gewichte"] = [str(random.randint(p, k)), str(random.randint(p, k)), str(random.randint(p, k)), str(random.randint(p, k))]
		stadtteil["gewichte"] = [stadtteil["name"]] + stadtteil["gewichte"]
		stadtteile.append(stadtteil)

numAutomaten = 12
automatenRadius = 75
attributNamen = ["Name"] + ["Einwohner_maennlich", "Einwohner_weiblich", "Einzahlungen_pro_Tag", "Abhebungen_pro_Tag"]
lines = []
lines.append(str(len(attributNamen)))
lines += attributNamen
lines.append(str(len(stadtteile)))
for stadtteil in stadtteile:
	lines.append(str(len(stadtteil["punkte"])))
	lines.append(" ".join(stadtteil["punkte"]))
	lines.append(" ".join(stadtteil["gewichte"]))
lines.append(str(numAutomaten))
lines.append(str(automatenRadius))
file = "\n".join(lines)
print(file)
