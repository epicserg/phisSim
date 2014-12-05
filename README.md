phisSim
=======

The proof of concept of akka clustering possiblities

This is a leader/follower(master/slave) pattern based cluster on akka.
Akka provides actor based message logic to the cluster.
Its job is to calculate phisical simulations in concurrent matter.

Below is the list of supported simulations:
    *object chunk position and movement calculation in 3D space gravity field using brute force euler method.


Simulations
============

object chunk movement calculation in 3D space with gravity using euler method
------------

The cluster should receive a set of objects of type PhysicalObject before starting a calculation.
PhysicalObject refers to a model which has got its mass, velocity and position in 3D space.
The calculation will run in cycles. Each cycle will calculate every model's position and velocity shifts
by a small period of time specified as dt.

All numeric operations are done using Apfloat library.


Architecture
============

Please refer to phisSimarchitecture.jpg to get an overview of architecture.
The architecture consists of 3 types of nodes : the leader ,the web node and workers(follower).
Although the leader and the web node are singletones, it is possible to add almost unlimited number
of worker nodes .

Leader node
----------

This is the center and the seed node of the cluster.
Because this is the seed node it means that it should be started first so other nodes can connect to it and
form a cluster.

Upon bootstrap the leader waits for workers and a web node to be connected.
The leader communicates with users via web node.

In case the leader receives a task to calculate it distributes the task among workers.
When the workers return all calculation task results,
the leader creates a new set of tasks and distributes them in the same manner.

The job distribution algorithm may vary depending on simulation needs


Web node
----------

Web node is a bridge between the user and the cluster.
The web project is for input and result rendering.
It will run in a spring container inside JEE server. (most likely Jetty)
The result will be sent to browser via websocket protocol and rendered in 3D webGl environment.
The user may start a calculation and input start data via a browser.


Worker(follower)
----------

The worker(follower) project computes the given tasks. You can add many workers to the leader.
The computation algorithm varies depending on used algorithm.


Shared directory
-----------
Shared is for holding common code.

Running
===========
You will need a fresh installment of gradle(at least gradle 2.1)
To run a cluster go to core and write gradle run.
Then run attach web and worker in different process.
You may use default config if you want everything to be run on the same computer.
Otherwise please check application.conf files to specife hosts and ports.






