SUMMARY = "An image containing the build system itself"
DESCRIPTION = "A VM image containing the build system that you can boot and run using VirtualBox."
#HOMEPAGE = "http://www.yoctoproject.org/documentation/poky-vm"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

IMAGE_INSTALL = "packagegroup-core-boot packagegroup-core-ssh-openssh \
                 packagegroup-poky-vm"
                 
IMAGE_FEATURES += "package-management"

#Extras size (in KB) 200M !!!!!!!!!!!! temporary !!!!!!!!!!!!
#IMAGE_ROOTFS_EXTRA_SPACE = "204800"

# Ensure there's enough space to do a core-image-sato build, with rm_work enabled
IMAGE_ROOTFS_EXTRA_SPACE = "41943040"

# Do a quiet boot with limited console messages
APPEND += "quiet rootfstype=ext4"

DEPENDS = "zip-native"
IMAGE_FSTYPES = "vdi"

inherit core-image
  
#SRCREV ?= "d9d5b8b499af3ae01a1e33d0d3969c54c2d4ab1b"
SRCREV ?= "d01cd53429b1c20f01dac97f1b9b659cb9dc9812"
SRC_URI = "git://git.yoctoproject.org/poky \
           file://README.txt \
           file://S99init-poky-vm.sh \
           file://add_bashrc \
           file://govbox.sh \
           file://govbox.bat \
          "
#S = "${WORKDIR}"

VMUSER="vmuser"

IMAGE_CMD_ext4_append () {
	# We don't need to reserve much space for root, 0.5% is more than enough
	tune2fs -m 0.5 ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext4
}

fakeroot do_populate_poky_src () {
	# Because fetch2's git's unpack uses -s cloneflag, the unpacked git repo
	# will become invalid in the target.

	rm -rf ${WORKDIR}/git/.git
	rm -f ${WORKDIR}/git/.gitignore

	cp -Rp ${WORKDIR}/git ${IMAGE_ROOTFS}/home/${VMUSER}/poky
	rm -rf ${IMAGE_ROOTFS}/home/${VMUSER}/poky/documentation

#	echo "/usr/bin" > ${IMAGE_ROOTFS}/home/${VMUSER}/poky/build/pseudodone
#	echo "INHERIT += \"rm_work\"" >> ${IMAGE_ROOTFS}/home/${VMUSER}/poky/build/conf/auto.conf

	mkdir -p ${IMAGE_ROOTFS}/home/${VMUSER}/pseudo
	echo "export PSEUDO_PREFIX=/usr" >> ${IMAGE_ROOTFS}/home/${VMUSER}/.bashrc
	echo "export PSEUDO_LOCALSTATEDIR=/home/${VMUSER}/pseudo" >> ${IMAGE_ROOTFS}/home/${VMUSER}/.bashrc
	echo "export PSEUDO_LIBDIR=/usr/lib/pseudo/lib64" >> ${IMAGE_ROOTFS}/home/${VMUSER}/.bashrc
	
	cat ${WORKDIR}/add_bashrc >> ${IMAGE_ROOTFS}/home/${VMUSER}/.bashrc
	chown ${VMUSER}:${VMUSER} ${IMAGE_ROOTFS}/home/${VMUSER}/pseudo
	chown -R ${VMUSER}:${VMUSER} ${IMAGE_ROOTFS}/home/${VMUSER}/poky

	mkdir -p ${IMAGE_ROOTFS}/mnt/shared-poky-vm
	chown -R ${VMUSER}:${VMUSER} ${IMAGE_ROOTFS}/mnt/shared-poky-vm
	mkdir -p ${IMAGE_ROOTFS}/mnt/yocto-tmp
	chown -R ${VMUSER}:${VMUSER} ${IMAGE_ROOTFS}/mnt/yocto-tmp
	
	# Assume we will need CDROM to install guest additions
	mkdir -p ${IMAGE_ROOTFS}/media/cdrom
	
	# Allow ${VMUSER} to use sudo to setup tap/tun
	echo "${VMUSER} ALL=(ALL) NOPASSWD: ALL" >> ${IMAGE_ROOTFS}/etc/sudoers

	cp ${WORKDIR}/S99init-poky-vm.sh ${IMAGE_ROOTFS}/etc/rcS.d/S99init-poky-vm.sh
}

IMAGE_PREPROCESS_COMMAND += "do_populate_poky_src; "

addtask rootfs after do_unpack

python () {
	# Ensure we run these usually noexec tasks
	d.delVarFlag("do_fetch", "noexec")
	d.delVarFlag("do_unpack", "noexec")
}

create_vdi_bundle_files () {
	cd ${WORKDIR}
	
	rm -rf ./yocto_poky_vm
	mkdir -p yocto_poky_vm
	chmod +x govbox.sh
	cp govbox.sh yocto_poky_vm
	cp govbox.bat yocto_poky_vm
	cp README.txt yocto_poky_vm
	ln -sf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.vdi yocto_poky_vm/yocto_poky_vm.vdi
	zip -r ${DEPLOY_DIR_IMAGE}/yocto_poky_vm-${DATETIME}.zip yocto_poky_vm
	ln -sf yocto_poky_vm-${DATETIME}.zip ${DEPLOY_DIR_IMAGE}/yocto_poky_vm.zip 

	rm ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hdddirect || true
	rm ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext4 || true
	# delete symlinks to deleted files
#	rm ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.hdddirect || true
#	rm ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4 || true
}

python do_bundle_vdi_files() {
    bb.build.exec_func('create_vdi_bundle_files', d)
}


# set root password as "root"
ROOTFS_POSTPROCESS_COMMAND += "set_root_passwd;"
set_root_passwd() {
   sed 's%^root:[^:]*:%root:wYNffsf6sozwE:%' \
       < ${IMAGE_ROOTFS}/etc/shadow \
       > ${IMAGE_ROOTFS}/etc/shadow.new;
   mv ${IMAGE_ROOTFS}/etc/shadow.new ${IMAGE_ROOTFS}/etc/shadow ;
}

addtask bundle_vdi_files after do_vmimg before do_build

do_bundle_files[nostamp] = "1"
