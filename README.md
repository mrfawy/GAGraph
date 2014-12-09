GAGraph
=======

it's graph generation tool , aimed to find a graph with max vertices  which covers  minimum area

The Goal was to solve a seating problem :

Problem
----------

We have n employee ,and at the time we need to have maximum number of offices in the same work space, while at the same time  we need to have maximum communication ( less distance between each 2 persons )

Find a way to place our offices that meets the goal 

Soution:
---------
it's a graph like problem , generate graphs , for each one calculate fitness value ( how much it meets the goal in terms of v and area or diameter) , like genetic algorithm only the fittest will survive till the next iteration .

 .

