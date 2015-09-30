DESCRIPTION = "A a package of utilities for doing and managing mounts of the Linux CIFS filesystem."
HOMEPAGE = "http://wiki.samba.org/index.php/LinuxCIFS_utils"

LICENSE = "GPLv3 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PR = "r1"

SRCREV = "353d491dcb5d69d31434abeb962c8e9a49c36867"
SRC_URI = "https://download.samba.org/pub/linux-cifs/cifs-utils/cifs-utils-${PV}.tar.bz2"
SRC_URI[md5sum] = "b7d75b67fd3987952896d27256c7293d"
SRC_URI[sha256sum] = "38fc63926af435dae4ebcf4406275580a692d9fb9ee3e32170317cf2ba68e6e3"

S = "${WORKDIR}/cifs-utils-${PV}"

PACKAGECONFIG ??= ""
PACKAGECONFIG[cap] = "--with-libcap,--without-libcap,libcap"
# when enabled, it creates ${bindir}/cifscreds and --ignore-fail-on-non-empty in do_install_append is needed
PACKAGECONFIG[cifscreds] = "--enable-cifscreds,--disable-cifscreds,keyutils"
# when enabled, it creates ${sbindir}/cifs.upcall and --ignore-fail-on-non-empty in do_install_append is needed
PACKAGECONFIG[cifsupcall] = "--enable-cifsupcall,--disable-cifsupcall,krb5 talloc keyutils"

inherit autotools pkgconfig

do_install_append() {
    # Remove empty /usr/bin and /usr/sbin directories since the mount helper
    # is installed to /sbin
    rmdir ${D}${bindir} ${D}${sbindir}
}

RRECOMMENDS_${PN} = "kernel-module-cifs"

