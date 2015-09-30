#!/bin/sh

if [ "$#" -eq 1 ]; then
   VM=$1
else
   VM='poky-vm-64bit'
fi

echo Starting VM:$VM

if VBoxManage list vms | grep -w "$VM" 
  then
    echo "VM $VM already exists."
  else
    VBoxManage createvm --name $VM --register
    if [ $? -ne 0 ] 
      then
        echo "Error creating VM $VM. (Please delete existing VM)."
        # VM is not registerd but exists. Must delete manually...
        # VBoxManage unregistervm $VM --delete
        exit 1
      else
        VBoxManage storagectl $VM --name SATA --add sata --controller IntelAhci --bootable on --portcount 1
	# We may consider guest additions at some point...
        # VBoxManage storagectl $VM --name "IDE Controller" --add ide
    fi
fi

VBoxManage modifyvm $VM --ostype Linux_64 --ioapic on --memory 1024 --cpus 2

VBoxManage storageattach $VM --storagectl SATA --port 0 --discard on --nonrotational on --type hdd --medium "yocto_poky_vm.vdi"
VBoxManage modifyvm $VM --nic1 bridged --bridgeadapter1 eth0 --cableconnected1 on

#VBoxManage storageattach $VM --storagectl "IDE Controller" --port 0 --device 0 --type dvddrive --medium /path/to/guest_extensions.iso

VBoxManage showvminfo $VM | grep 'Storage Controller Name'
VBoxManage showvminfo $VM | grep 'Memory size'
VBoxManage showvminfo $VM | grep 'Number of CPUs'

VBoxManage startvm $VM
