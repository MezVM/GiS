class Logger:
    def __init__(self, name):
        self.name = name.__str__()

    def log(self, msg):
        newMsg = "\t"+self.name.__str__() + ": " + msg.__str__()
        print(newMsg.__str__())
