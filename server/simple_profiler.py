import time

class SimpleProfiler:
    def __init__(self):
        self.clocks = {}
        self.off = False
    
    def off(self):
        self.off = True

    def start(self,name):
        if not self.off:
            self.clocks[name] = time.time()
    
    def stop(self,name):
        if not self.off:
            assert name in self.clocks
            start_time = self.clocks[name]
            del self.clocks[name]
            end_time = time.time()
            return end_time - start_time

    def print_result(self,name,value):
        print name,'completed in',value*1000.0,'ms'

    def pstop(self,name):
        if not self.off:
            self.print_result(name, self.stop(name))
        
