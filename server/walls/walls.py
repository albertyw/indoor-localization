from PIL import Image
import pickle

img = Image.open('edited_map.png')
print img
(minX, maxX) = (5800, 0)
(minY, maxY) = (780, 0)
wall_list = []
for x in range(5800):
    for y in range(780):
        pixel = img.getpixel((x,y))
        if pixel[0] - pixel[1] > 100:
            wall_list.append((x,y))

with open('walls.p', 'w') as outfile:
  pickle.dump(frozenset(wall_list), outfile)
