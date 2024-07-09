#ifndef ITEM_H
#define ITEM_H

#include <iostream>
#include <cstdlib>
#include <string>
#include <math.h>
#include <time.h>  // Used by timing functions
using namespace std;

#include "list.h"

static string maxKey = "";

typedef int Key;

class Item 
  { 
    private:
      string nome;
      int codice;
      float velocita;
      string categoria;
    public:
      Item(int c=0,string n=maxKey,string cat=maxKey,float v=0.0)
      { nome=n; codice=c;velocita=v;categoria=cat; }
 
      string getnome()const{return nome;}
      int getcodice()const{return codice;}
      float getvelocita()const{return velocita;}
      string getcategoria()const{return categoria;}
      
      void setvelocita(){
          velocita=velocita+1;}
      
      int getabsvelocita()const{return fabs(velocita);}
      
      int null()
        { return nome == maxKey; }
	  		
      void show(ostream& os = cout)
        { os <<"show "<< nome << " " << codice << endl; }

   }; 

inline ostream& operator<<(ostream& s, const Item& i)
{ return s <<"("<< i.getcodice()<<","<<i.getnome()<<","<<i.getcategoria()<<","<<i.getvelocita()<<")";}
  
inline void Assert(bool val, string s) {
  if (!val) { // Assertion failed -- close the program
    cout << "Assertion Failed: " << s << endl;
    exit(-1);
  }
}

template <typename E>
void lprint(List<E>& L) {
  int currpos = L.currPos();

  L.moveToStart();

  cout << "< ";
  int i;
  for (i=0; i<currpos; i++) {
    cout << L.getValue() << " ";
    L.next();
  }
  cout << "| ";
  while (L.currPos()<L.length()) {
    cout << L.getValue() << " ";
    L.next();
  }
  cout << ">\n";
  L.moveToPos(currpos);
}

#endif
