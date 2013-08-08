from PIL import Image
import json
img = Image.open('edited_map.png')
print img
(minX, maxX) = (5800, 0)
(minY, maxY) = (780, 0)
wall_list = []
for x in range(5800):
    for y in range(780):
        pixel = img.getpixel((x,y))
        if pixel[0] - pixel[1] > 100:
            if x < minX:
                minX = x
            if x > maxX:
                maxX = x
            if y < minY:
                minY = y
            if y > maxY:
                maxY = y
            wall_list.append((x,y))

print (minX, maxX)
print (minY, maxY)

with open('walls.txt', 'w') as outfile:
  json.dump(wall_list, outfile)
