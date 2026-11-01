# Assignment 3. Maze Escape

Student ID: 212521
Name: <ВПИШИ ИМЯ>
Group: <ВПИШИ ГРУППУ>
Maze size (rows x columns): 10 x 8

This file is the report. How to start: `START_HERE.md`. The task: `docs/Assignment3.pdf`.

## Program output

The 13 lines my program prints on my data (`java -cp out Main`):

    Broken: 79
    Kept: 63
    Last broken wall: 131
    MST cost: 21478
    Heaviest broken wall: 835
    Path length: 20
    DFS visited: 38
    Dead ends: 26
    Zones: 80
    Largest zone: 1
    Is DAG: 1
    Flow stages: 18
    Fastest route cost: 21

My maze:

![maze](output/maze.png)

## Questions

Mission 0. What do find and union do, and what makes them fast in your code?

<ТВОИ СЛОВА>

Mission 1. Why is Broken always R*C - 1, and Kept always (R-1)*(C-1)?

<ТВОИ СЛОВА>

Mission 2. My MST cost from Kruskal and from Prim, and why they are equal:

Both give 21478. <ТВОИ СЛОВА: почему равны>

Mission 3. Why is there exactly one path between two cells? How did you avoid a stack overflow on a huge maze?

<ТВОИ СЛОВА>

Mission 4. Why does the second pass of Kosaraju go on the reversed graph? Why do the slides have no cycle exactly when Zones equals the number of cells?

<ТВОИ СЛОВА>

Mission 5. Why is it enough to go through the zones in the order of their numbers? What does this pass cost?

<ТВОИ СЛОВА>

## AI use

Tool: Claude (Anthropic). I used it to write a first version of the code in DisjointSet, Maze, Escape, Zones and the extra checks in DisjointSetTest, and to explain the ideas behind the algorithms. I read the code, ran `tools/Check.java` and my own data, and I can explain every line. The answers in the Questions section are written by me.
<ПРОВЕРЬ, ЧТО ЭТО ПРАВДА, И ИЗМЕНИ ПОД СЕБЯ, ЕСЛИ ДЕЛАЛ ИНАЧЕ>

## Timing (Mission 7)

Milliseconds, 5 runs each, from `java -cp out Bench`.

| maze | Kruskal | Prim |
|---|---|---|
| 100 x 100 | <avg> | <avg> |
| 300 x 300 | <avg> | <avg> |
| 1000 x 1000 | <avg> | <avg> |

What do the numbers say about how the time grows? Two or three sentences.

<ТВОИ СЛОВА: сравни рост при увеличении размера, у какого алгоритма быстрее и почему>

## Complexity

n is the number of cells, m the number of walls (or slides). z is the number of zones.

| algorithm | time | space | assumptions |
|---|---|---|---|
| DSU find and union | O(alpha(n)) amortized per operation, almost constant | O(n) | union by size and path compression together |
| Kruskal | O(m log m) | O(n + m) | sorting the walls dominates; DSU part is about O(m alpha(n)) |
| Prim | O(m log m) | O(n + m) | binary heap (PriorityQueue), lazy deletion: up to 2m entries |
| DFS | O(n + m) | O(n) | iterative, own stack array; every cell and open door is looked at a constant number of times |
| Kosaraju | O(n + m) | O(n + m) | two DFS passes; adjacency lists of slides and of reversed slides |
| Route in the zone graph | O(n + m) | O(n + m) | one pass over zones in topological order, each slide relaxed once; no priority queue |

## Before you submit

- data/walls.txt, data/slides.txt and output/maze.png are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
