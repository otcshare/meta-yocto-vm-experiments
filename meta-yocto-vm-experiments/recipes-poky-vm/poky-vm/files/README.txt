
This assumes VirtualBox has already been installed.


Start VM
========
To get started quickly, you can run govbox.sh from a terminal.
Feel free to edit the file to your preferences.
(On Windows, run govbox.bat)


SSH Connections
===============
Network interface inside VM uses udhcpc, so you need to
find the IP address by using console:

    # ifconfig

You should be able to connect via SSH:

root, password "root"
vmuser, password "password"


Samba Shares
============
You should see two shared Samba folders on your network
(workgroup WORKGROUP)

\\YOCTO-poky-vm

Shared folders have "vmuser" permissions only.

On Linux you can find the shares using:
  $ nmblookup -S WORKGROUP 
  
Browse the folders:
  $ smbclient //[IP ADDRESS]/YoctoVmuser
  $ smbclient //[IP ADDRESS]/YoctoRoot


Proxies
=======
To set up networking for bitbaking, you may need to 
modify the existing proxies in the file /home/vmuser/.bashrc

Presently the proxies are set as:

export http_proxy=http://proxy-jf.intel.com:911
export https_proxy=https://proxy-jf.intel.com:911

I was able to run bitbake core-image-minimal in the VM, 
so the proxies and network are known to work. 
The VM grew to about 22GB after the baking was finished.
