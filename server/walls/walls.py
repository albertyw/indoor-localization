from PIL import Image
import pickle

img = Image.open('skinny.png')
wall_list = []
count = 0
for x in range(5800):
    for y in range(780):
        pixel = img.getpixel((x,y))
        if pixel[0] - pixel[1] > 100:
            wall_list.append((x,y))
            count += 1
print count
with open('walls.p', 'w') as outfile:
  pickle.dump(frozenset(wall_list), outfile)
