phisSim
=======

The proof of concept of akka clustering possiblities

This is a leader/follower pattern based cluster on akka.

The cluster should compute phisical simulations.
The core project is for creating and routing tasks and acts as a cluster seed.

The worker(follower) project computes the given tasks. You can add many followers.

Shared is for holding common code.

The web project is for input and result rendering.


To run a cluster go to core and write gradle run.


