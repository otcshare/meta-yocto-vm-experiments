SUMMARY = "New user to do specific job"
DESCRIPTION = "This recipe create a new user named ${PN}, who is used for specific jobs like building."
PR = "r0"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://start_ssh.sh"

S = "${WORKDIR}"

inherit useradd

# user password is "password" (encrypted)
# openssl passwd -crypt password

USER_PASSWORD ?= "e7TeJQnIq1l72"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --create-home \
                       --groups video,tty,audio \
                       --password ${USER_PASSWORD} \
                       --user-group ${PN}"

do_install () {
        echo "3: Here: ${USER_PASSWORD}" >> ~/Documents/jb-chef.log
    install -d -m 755 ${D}/home/${PN}/
    install -p -m 755 start_ssh.sh ${D}/home/${PN}/
    chown ${PN}.${PN} ${D}/home/${PN}/start_ssh.sh
}

FILES_${PN} += "/home/${PN}/start_ssh.sh"
