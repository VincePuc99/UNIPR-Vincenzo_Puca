// From the software distribution accompanying the textbook
// "A Practical Introduction to Data Structures and Algorithm Analysis,
// Third Edition (C++)" by Clifford A. Shaffer.
// Source code Copyright (C) 2007-2011 by Clifford A. Shaffer.

// Functions for creating and printing graphs

#ifndef GRAPHUTIL
#define GRAPHUTIL

#include <iostream> 
#include <fstream> 
#include <string> 
using namespace std;

#include "graph.h"

inline void Gprint(Graph* G) {
  int i, j;

  cout << "Number of vertices is " << G->n() << "\n";
  cout << "Number of edges is " << G->e() << "\n";

  cout << "Matrix is:\n";
  for (i=0; i<G->n(); i++) {
    for(j=0; j<G->n(); j++)
      cout << G->weight(i, j) << " ";
    cout << "\n";
  }
}

// Create a graph from file fid
template <typename GType>
GType* createGraph(ifstream& fid) {
  bool undirected;  // true if graph is undirected, false if directed
  int N;
  int v1, v2, dist;
  char c;

  fid >> N;
  GType* G = new GType(N);

  fid >> c;
  if (c == 'U')
    undirected = true;
  else if (c == 'D')
    undirected = false;
  else {
    cout << "Bad graph type" << endl;
    return NULL;
  }

  // Read in edges
  while (fid >> v1 >> v2) {
	dist = 1;
	
    G->setEdge(v1, v2, dist);
	
    if (undirected) // Put in edge in other direction
      G->setEdge(v2, v1, dist);
	
  }
  return G;
}

#endif
