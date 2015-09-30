echo off

rem Edit the following based on your HW
set VM=poky-vm-64bit
set VM_memory=1024
set VM_cpus=4

set VBoxManage="C:\Program Files\Oracle\VirtualBox\VBoxManage.exe"
%VBoxManage% list vms | findstr %VM%

IF ERRORLEVEL 1 GOTO createVM

:successLabel
REM put code to execute in case of success here.
ECHO VM %VM% already created...
GOTO endLabel

:createVM
REM Create and register the VM
ECHO Creating a new VM
%VBoxManage% createvm --name %VM% --register
rem TODO Check for return error code
%VBoxManage% storagectl %VM% --name SATA --add sata --controller IntelAhci --bootable on --portcount 1
REM We may consider guest additions at some point...
REM %VBoxManage% storagectl %VM% --name "IDE Controller" --add ide
:endLabel


%VBoxManage% modifyvm %VM% --ostype Linux_64 --ioapic on --memory %VM_memory% --cpus %VM_cpus%

%VBoxManage% storageattach %VM% --storagectl SATA --port 0  --discard on --nonrotational on --type hdd --medium "yocto_poky_vm.vdi"

rem eth0 does not seem to exist on Windows
rem %VBoxManage% modifyvm %VM% --nic1 bridged --bridgeadapter1 eth0 --cableconnected1 on
%VBoxManage% modifyvm %VM% --nic1 bridged --bridgeadapter1 "Realtek PCIe GBE Family Controller" --cableconnected1 on

rem We may need IDE for guest extensions, thou it should be possible to use SATA for CDROM as well
rem #VBoxManage% storageattach %VM% --storagectl "IDE Controller" --port 0 --device 0 --type dvddrive --medium /path/to/guest_extensions.iso

%VBoxManage% showvminfo %VM% | findstr/c:"Storage Controller Name"
%VBoxManage% showvminfo %VM% | findstr/c:"Memory size"
%VBoxManage% showvminfo %VM% | findstr/c:"Number of CPUs"

%VBoxManage% startvm %VM%
