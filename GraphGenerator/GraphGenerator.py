import random
import matplotlib.pyplot as plt
import networkx as nx
from Logger import Logger
import itertools
import argparse

def randomK5(G):
    # log = Logger("K5")
    # log.log("Generowanie wierzcholkow.")
    candidates = random.sample(G.nodes, 5)
    # log.log("Kandydaci: " + candidates.__str__())
    pairs = list(itertools.combinations(candidates, 2))
    # log.log("Krawędzie: " + pairs.__str__())
    G.add_edges_from(pairs)
    # log.log("Krawędzie dodano.")
    return pairs

def randomK33(G):
    # log = Logger("K33")
    # log.log("Generowanie wierzcholkow.")
    candidates = set(random.sample(G.nodes, 6))
    firstSet = random.sample(candidates, 3)
    secondSet = candidates.difference(firstSet)
    # log.log("Kandydaci: s1:" + firstSet.__str__()+ " s2: "+secondSet.__str__())
    pairs = set(itertools.product(firstSet, secondSet))
    # log.log("Krawędzie: " + pairs.__str__())
    G.add_edges_from(pairs)
    # log.log("Krawędzie dodano.")
    return pairs

# if __name__ == "__main__":
#     log = Logger("Main")
#     parser = argparse.ArgumentParser(description='Create graphs.')
#     parser.add_argument('size', help="number of nodes.", type=int)
#     parser.add_argument('dens', help="density of edges.", type=float)
#     parser.add_argument('--none', help="none", action="store_true")
#     parser.add_argument('--k5', help="k5", action="store_true")
#     parser.add_argument('--k33', help="k33", action="store_true")
#     args = parser.parse_args()
#     density = args.dens
#     size = args.size
#     randomGraph = nx.erdos_renyi_graph(size, density)
#     if args.none:
#         log.log("no additional graphs.")
#         appender = "_none"
#     elif args.k5:
#         log.log("K5 selected.")
#         pairs = randomK5(randomGraph)
#         appender = "_K5"
#     elif args.k33:
#         log.log("K33 selected.")
#         pairs = randomK33(randomGraph)
#         appender = "_K33"
#     else:
#         log.log("No mode selected")
#         exit(1)
#     matrix = nx.adjacency_matrix(randomGraph).toarray()
#     print(matrix.__str__())
#     size_appender = "_"+size.__str__()
#     hash=matrix.__str__().__hash__()
#     file_object = open("graph{0}{1}{2}.txt".format(hash,size_appender, appender), "w")
#     file_object.write(size.__str__() + "\n")
#     for row in matrix:
#         newLine = row.__str__().replace("\n", "")
#         file_object.write(newLine[1:len(newLine)-1]+"\n")
#     file_object.close()
#     log.log("file [ " + file_object.name.__str__() + " ] created.")

if __name__ == "__main__":
    log = Logger("Main")
    appender = "_none"
    for i in range(0, 250):
        size = random.randrange(900, 2000, 1)
        density = round(random.uniform(0.1, 0.25),2)
        randomGraph = nx.erdos_renyi_graph(size, density)
        matrix = nx.adjacency_matrix(randomGraph).toarray()
        # print(matrix.__str__())
        size_appender = "_size-"+size.__str__()+"_dens-"+density.__str__()
        hash=i.__str__()
        file_object = open("none/graph{0}{1}{2}.txt".format(hash,size_appender, appender), "w")
        file_object.write(size.__str__() + "\n")
        for row in matrix:
            newLine = ""
            for element in row:
                newLine += element.__str__()+" "
            file_object.write(newLine[0:len(newLine)-1]+"\n")
        file_object.close()
        log.log("file [ " + file_object.name.__str__() + " ] created. ["+i.__str__()+"/250]")
    appender = "_K5"
    for i in range(0, 250):
        size = random.randrange(900, 2000, 1)
        density = round(random.uniform(0.1, 0.25), 2)
        randomGraph = nx.erdos_renyi_graph(size, density)
        pairs = randomK5(randomGraph)
        matrix = nx.adjacency_matrix(randomGraph).toarray()
        # print(matrix.__str__())
        size_appender = "_size-" + size.__str__() + "_dens-" + density.__str__()
        hash = i.__str__()
        file_object = open("k5/graph{0}{1}{2}.txt".format(hash, size_appender, appender), "w")
        file_object.write(size.__str__() + "\n")
        for row in matrix:
            newLine = ""
            for element in row:
                newLine += element.__str__() + " "
            file_object.write(newLine[0:len(newLine) - 1] + "\n")
        file_object.close()
        log.log("file [ " + file_object.name.__str__() + " ] created. [" + i.__str__() + "/250]")




    # randomGraph = nx.erdos_renyi_graph(30, 0.07)
    # pairs = randomK33(randomGraph)
    # nodes = list(randomGraph.nodes)
    # # shells = [nodes[x:x + 10] for x in range(0, len(nodes), 10)]
    # # pos = nx.shell_layout(randomGraph,shells,scale=2)
    # pos = nx.circular_layout(randomGraph)
    # nx.draw_networkx_nodes(randomGraph, pos, with_labels=True,node_size=10,alpha=0.8, node_color='b')
    # nx.draw_networkx_edges(randomGraph, pos,
    #                        edgelist=randomGraph.edges, edge_color='g')
    # nx.draw_networkx_edges(randomGraph, pos,
    #                        edgelist=pairs, edge_color='r',width=4,alpha=1)
    # plt.show()



# print(G.nodes)
# print(G.edges)
#
# print(randomGraph.nodes)
# uuu = nx.compose(G, randomGraph)


# print(G.adj)
# print(nx.adjacency_matrix(G))
# asd = nx.adjacency_matrix(G)
# nx.draw_circular(uuu, with_labels=True, font_weight='bold')
# print(asd.todense())
# nx.draw_random(G)

# nx.draw_circular(er)
# nx.draw_spectral(G)

# plt.show()
