cloud
=====

cloud resource management research

this is a simple implemention about how to apply the AOS to the allocating cloudlets to vms in shortest time issue;
here are some hypothesis:
1.vm use timeSharedSchedule, which means that the cloudlets allocated on this vm will be accomplished one by one, instead of
  in the same time,which is much more easier for this modeling.
2.cloudlets' operating time can be initialized through the vms and the cloudlets' parameters, for vms is the CPU, and for
cloudlets is the PE(both of them are provided in the simulation tool CloudSim)

these hypothesis will be improved in future work.
