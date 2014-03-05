from __future__ import print_function, division

import pygame
from pygame.rect import *
from pygame import camera
from PIL import Image

import math
import sys
import os
import colorsys


class ViewingWindow(object):
    def __init__(self, image):
        self.image = image
        self.screen = pygame.display.set_mode(self.image.get_size())
        self.clock = pygame.time.Clock()
        self.selections = []
        self.current_sel = None
        self.mincolor = (0,0,0)
        self.maxcolor = (1,1,1)

    def draw(self):
        self.screen.fill((0,0,0))
        self.screen.blit(self.image, (0,0))
        for s in self.selections:
            s.draw(self.screen, pygame.mouse.get_pos())
        pygame.display.flip()

    def drawmask(self):
        self.screen.fill((0,0,0))
        self.screen.blit(self.image, (0,0))
        for s in self.selections:
            if s.finished:
                s.drawmask(self.screen)
        pygame.display.flip()

    def run(self, im):
        self.image = im
        ms = self.clock.tick(60)
        self.draw()
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                sys.exit()
            elif event.type == pygame.MOUSEBUTTONDOWN:
                if event.button == 1:
                    if not self.current_sel:
                        self.add_selection(PolySelection())
                    done = self.current_sel.add_point(event.pos)
                    if done:
                        self.end_selection()
                elif event.button == 3:
                    if not self.current_sel:
                        self.add_selection(CircleSelection())
                        self.current_sel.add_point(event.pos)
                    else:
                        self.current_sel.end(event.pos)
                        self.end_selection()
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_z and (event.mod|pygame.KMOD_CTRL):
                    self.undo()
                elif event.key == pygame.K_BACKSPACE:
                    self.clear_selections()
                else:
                    self.print_stats()

    def add_selection(self, sel):
        self.current_sel = sel
        self.selections.append(sel)

    def end_selection(self):
        self.current_sel = None
        self.update_stats()

    def clear_selections(self):
        self.current_sel = None
        self.selections = []

    def undo(self):
        self.selections.pop(-1)
        if self.current_sel:
            self.current_sel = None
        self.update_stats()

    def get_pixels(self):
        rect = self.selections[0].get_rect()
        rect.unionall_ip([s.get_rect() for s in self.selections])
        sx,sy,sw,sh = rect
        surf = pygame.Surface(self.image.get_size())
        surf.fill((0,0,0))
        width = surf.get_width()
        height = surf.get_height()
        for s in self.selections:
            if s.finished:
                s.drawmask(surf)
        pixels = set(((x,y)
                      for x in xrange(sx, sx+sw)
                      for y in xrange(sy, sy+sh)
                     if 0 <= x < width and 0 <= y < height and surf.get_at((x,y))[0] > 0))
        return pixels

    def update_stats(self):
        pixels = self.get_pixels()
        img = self.image
        maxH = 0.0
        minH = 1.0
        maxS = 0.0
        minS = 1.0
        maxV = 0.0
        minV = 1.0
        for pix in pixels:
            c = img.get_at(pix)
            h,s,v = colorsys.rgb_to_hsv(c[0]/255, c[1]/255, c[2]/255)
            maxH = max(maxH, h)
            minH = min(minH, h)
            maxS = max(maxS, s)
            minS = min(minS, s)
            maxV = max(maxV, v)
            minV = min(minV, v)
        self.mincolor = (minH, minS, minV)
        self.maxcolor = (maxH, maxS, maxV)
        print("Selected {0} pixels.".format(len(pixels)))
        self.print_stats()

    def print_stats(self):
        print("Minimum color (HSB): {0}".format(ivmul(self.mincolor, (180,255,255))))
        print("Maximum color (HSB): {0}".format(ivmul(self.maxcolor, (180,255,255))))
        print("")
            


class Selection(object):
    def __init__(self):
        self.finished = False
        
    def draw(self, surf, mousepos):
        pass

    def drawmask(self, surf):
        pass

    def add_point(self, point):
        pass

    def end(self, point):
        pass

    def get_rect(self):
        pass

class CircleSelection(Selection):
    def __init__(self):
        super(CircleSelection, self).__init__()
        self.start = None
        self.radius = 0

    def draw(self, surf, mousepos):
        color = (0,255,0) if self.finished else (0,0,255)
        rad = self.radius if self.finished else int(mag(disp(self.start, mousepos)))
        if rad < 2:
            return
        pygame.draw.circle(surf, color, self.start, rad, 1)

    def drawmask(self, surf):
        pygame.draw.circle(surf, (255,255,255), self.start, self.radius)

    def add_point(self, point):
        if (self.start is None):
            self.start = point
        else:
            self.radius = int(mag(disp(self.start, point)))
            self.finished = True
        return self.finished

    def end(self, point):
        return self.add_point(point)

    def get_rect(self):
        x,y = self.start
        r = self.radius
        return Rect(x-r, y-r, r*2, r*2)

class PolySelection(Selection):
    def __init__(self):
        super(PolySelection, self).__init__()
        self.points = []

    def draw(self, surf, mousepos):
        color = (0,255,0) if self.finished else (0,0,255)
        points = self.points + [mousepos] if not self.finished else self.points
        pygame.draw.aalines(surf, color, self.finished, points)

    def drawmask(self, surf):
        pygame.draw.polygon(surf, (255,255,255), self.points)

    def add_point(self, point):
        self.points.append(point)
        return False

    def end(self, point):
        if len(self.points) < 2:
            self.points.append(point)
        self.finished = True
        return True

    def get_rect(self):
        rects = [Rect(x,y,1,1) for x,y in self.points]
        rect = rects[0].unionall(rects)
        return rect
        
def disp(p1, p2):
    return (p2[0] - p1[0], p2[1] - p1[1])

def mag(vec):
    return math.sqrt(sum((i*i for i in vec)))

def vmul(a, b):
    return tuple((a[i]*b[i] for i in xrange(len(a))))

def ivmul(a, b):
    return tuple(int(i) for i in vmul(a,b))

def load_image(filename):
    print("Loading "+filename)
    # A workaround for DICE
    image = Image.open(filename)
    surf = pygame.image.fromstring(image.tostring(), image.size, image.mode)
    print("Loaded")
    return surf


if __name__ == "__main__":
    ## comment this out for camera
    filename = sys.argv[1]
    static_img = load_image(filename)   
    ## comment this out for camera
    pygame.init()
#    camera.init()
#    cam = camera.Camera("/dev/video0", (640, 480))
#    cam.start()
#    im = cam.get_image()
    main = ViewingWindow(static_img)
    while True:
        #im = cam.get_image()
        main.run(static_img)
