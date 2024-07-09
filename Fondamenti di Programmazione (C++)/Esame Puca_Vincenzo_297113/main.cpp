//
//  main.cpp
//  Puca_Vincenzo_297113
//
//  Created by Vincenzo Puca on 06/07/21.
//

#include <iostream>
#include <fstream>
#include <stdio.h>
#include "graph.h"
#include <string.h>
#include "item.h"
#include "llist.h"
#include <list>
#include "graphutil.h"
#include "grlist.h"

using namespace std;

void showlist(list <Item> g,string nomelista)
{
    list <Item> :: iterator it;
    cout <<"Lista "<<nomelista<< "(";
    
    for(it = g.begin(); it != g.end(); ++it)
       cout<< *it <<",";
    
    cout<<")";
    cout << '\n';
}

void merge(LList<Item>& Lm, LList<Item>& La, LList<Item>& Lb) {
    La.moveToStart();
    Lb.moveToStart();
    Item removed;
    int N = La.length();
    int M = Lb.length();
    for (int k = 0; k < N + M; k++)
    {
        if (La.currPos() == La.length())
        {
            removed = Lb.remove();
            Lm.append(removed);
            continue;
        }
        if (Lb.currPos() == Lb.length())
        {
            removed = La.remove();
            Lm.append(removed);
            continue;
        }
        if ((string)(La.getValue().getnome()) > ((string) Lb.getValue().getnome()))
            removed = La.remove();
        else
            removed = Lb.remove();
        Lm.append(removed);
    }
}

void mergesort(LList<Item>& Lm)
{
    Lm.moveToStart();
    if (Lm.length() < 2) return;
    LList<Item> La, Lb;
    Item removed;
    int N = Lm.length();
    for (int k = 0; k<N; k++) {
        removed = Lm.remove();
        if (k % 2) La.append(removed);
        else Lb.append(removed);
    }
    mergesort(La);
    mergesort(Lb);
    merge(Lm, La, Lb);
}

int main(int argc, const char * argv[]) {
    
    LList<Item> Ltreni;
    string nome;
    string codice;
    string velocita;
    string categoria;
    
    cout<<"Q1";
    ifstream myfile("/Users/vincenzopuca/Desktop/Puca_Vincenzo_297113/Puca_Vincenzo_297113/treni.txt");

    if(myfile.is_open()){
        cout<<"File aperto"<<endl;
        
        while (getline(myfile, codice, '/')) {
            getline(myfile, nome, '/');
            getline(myfile, categoria, '/');
            getline(myfile, velocita);

            int covcod=stoi(codice);
            float convvel=stof(velocita);
            Item temp(covcod, nome, categoria,convvel);
            
            Ltreni.append(temp);
        }
        
    }else{cout<<"File non aperto (Controllare Percorso File)"<<endl;}
    
    mergesort(Ltreni);
    cout<<"L1: "<<endl;
    lprint(Ltreni);
    cout<<"TEST VELOCITA'"<<endl<<endl;
    Item tempprova(43,"X50","Regina",200.4);
    
    Ltreni.setElement(tempprova);
    lprint(Ltreni);
    
    list <Item> L1, L2;
    
    for(Ltreni.moveToStart();Ltreni.currPos()<Ltreni.length();Ltreni.next())
    {
        Item currentitem;
        currentitem=Ltreni.getValue();
        if(currentitem.getcategoria()=="Pendolino"){
            L1.push_back(currentitem);
        }
        if(currentitem.getcategoria()=="TGV"){
            L2.push_front(currentitem);
        }
    }
    
    while (L1.size()>L2.size()) {
            L1.pop_back();
    }

    while (L2.size()>L1.size()) {
        L2.pop_back();
    }
    cout<<endl;
    cout<<"Q2: ";
    showlist(L1,"L1");
    cout<<"Q2: ";
    showlist(L2,"L2");

    cout<<endl<<"Q3 stampa lista: ";
    
    for(Ltreni.moveToStart();Ltreni.currPos()<Ltreni.length();Ltreni.next()){
      
        if(Ltreni.currPos()==0){
//            do nothing
        }else if(Ltreni.currPos()==Ltreni.length()){
            break;
        }else{
            Item curr=Ltreni.getValue();
            int absvelcurr=curr.getabsvelocita();
            
            Ltreni.prev();
            Item pre=Ltreni.getValue();
            int absvelprev=pre.getabsvelocita();
            
            Ltreni.next(); //return to home
            Ltreni.next(); //next
            Item succ=Ltreni.getValue();
            int absvelnex=succ.getabsvelocita();
            
            int diffnex=absvelnex-absvelcurr;
            int diffprev=absvelprev-absvelcurr;
            Ltreni.prev();//backtohome
            
            if(fabs(diffnex)<100 && fabs(diffprev)<100){
                Ltreni.remove();
                Ltreni.next();
            }
            
        }
    }
    
    lprint(Ltreni);
    
    cout<<endl<<endl;
    cout<<"Q4 ";
    
    Graph* G;

    ifstream miofile("/Users/vincenzopuca/Desktop/Puca_Vincenzo_297113/Puca_Vincenzo_297113/grafo.gph");

    G = createGraph<Graphl>(miofile);
    if (G == NULL) {
      cout << "Unable to create graph\n";
      exit(-1);
    }
    
    Gprint(G);

    G->setEdge(1, 2, 3);
    G->setEdge(2, 3, 3);
    G->setEdge(3, 1, 3);
    G->setEdge(4, 5, 3);
    G->setEdge(5, 6, 3);
    G->setEdge(6, 4, 3);
    
    Gprint(G);

    cout << "Number of vertices is " << G->n() << "\n";
    cout << "Number of edges is " << G->e() << "\n";
    
    
    return 0;
}
