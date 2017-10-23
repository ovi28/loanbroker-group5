##  Bottlenecks
In the current state, the system is relying on all the banks replying in time and it will not give the user an answer until all the banks have replied. If one of the banks is down or slower than the others, the system has to wait for it. In order to overcome this issue, the banks that are unresponsive should just be ignored.

## Testable
Thanks to the modularity of the program, every component is a different entity which doesn't rely on any other component and can be removed without affecting the other parts of the software. This makes the whole system easily testable.

## Web services
Besides the RabbitMQ messaging part of the system, it is relying on a couple of SOAP webservices made in PHP. One of them is for a bank while the other one is for the rulebase. To make the process of creating the webservices easier, we used the nusoap php library which provides all the necessary functionality.

## Quick description
The "Soap servers" files should be hosted on a server that supports php.
The server Java application is responsible for one of the banks and it is recommended to run it from inside intelliJ.
The client Java application is the main part of the program and it is recommended to run it from inside intelliJ.