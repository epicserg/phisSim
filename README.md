phisSim
=======

The proof of concept of akka clustering possiblities

This is a leader/follower(master/slave) pattern based cluster on akka.
Akka provides actor based message logic to the cluster.
Its job is to calculate phisical simulations in concurrent matter.
All floating point operations are done using Apfloat library.

Below is the list of supported simulations:


1)Object chunk position and movement calculation in 3D space gravity field using brute force euler method.


Simulations
============

Object chunk movement calculation in 3D space with gravity using euler method
------------

The cluster should receive a set of instances before starting a calculation.
Each instance refers to a model with its own mass, velocity and position in 3D space.

The calculation will run in cycles. Each cycle will calculate every instance    's position and velocity shifts
by a small period of time specified (dt) .



Architecture
============

Please refer to phisSimarchitecture.jpg to get an overview of architecture.
The architecture consists of three types of nodes : the leader ,the web node and workers(followers).
Although the leader and the web node are singletones, it is possible to add almost unlimited number
of worker nodes .

Leader node
----------

This is the central and the seed node of the cluster.
Because this is the seed node it should be started first so other nodes can connect to it and
form a cluster.

Upon bootstrap the leader waits for workers and a web node to be connected.
The leader communicates with users via web node.

In case the leader receives a simulation task it distributes the task among workers.
When the workers return all calculation task results,
the leader creates a new set of tasks and distributes them in the same manner.

The job distribution algorithm may vary depending on simulation needs


Web node
----------

Web node is a bridge between the user and the cluster.
It is for data input and result rendering.
It will run in a spring container inside a JEE server. (most likely Jetty)
The result will be sent to browser via websocket protocol and rendered in 3D webGl environment.
The user may start a calculation and input start data via a browser.


Worker(follower)
----------

The worker(follower) project computes the given tasks. You can add many workers to the leader.
The computation algorithm varies depending on simulation.


Shared directory
-----------
Shared is for holding common code.

Running
===========
You will need a fresh installment of gradle(at least gradle 2.1) and java SE 7
To run a cluster go to core directory in the terminal and write gradle run.
Then do the same in web and worker directories.
You may use default config if you want everything to be run on the same computer.
Otherwise please check application.conf files to specify hosts and ports.






