#
# Copyright (C) 2015 Intel Corporation
#

SUMMARY = "Self-hosting"
DESCRIPTION = "Packages required to run the build system"
PR = "r13"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-poky-vm \
    packagegroup-poky-vm-debug \
    packagegroup-poky-vm-sdk \
    packagegroup-poky-vm-extended \
    packagegroup-poky-vm-misc \
    packagegroup-poky-vm-host-tools \
    "

RDEPENDS_packagegroup-poky-vm = "\
    packagegroup-poky-vm-debug \
    packagegroup-poky-vm-sdk \
    packagegroup-poky-vm-extended \
    packagegroup-poky-vm-misc \
    packagegroup-poky-vm-host-tools \
    "

RDEPENDS_packagegroup-poky-vm-host-tools = "\
    connman \
    connman-plugin-ethernet \
    dhcp-client \
    e2fsprogs \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs \
    e2fsprogs-tune2fs \
    hdparm \
    iptables \
    lsb \
    mc \
    mc-fish \
    mc-helpers \
    mc-helpers-perl \
    mc-helpers-python \
    parted \
    pseudo \
    screen \
    "

RRECOMMENDS_packagegroup-poky-vm-host-tools = "\
    kernel-module-tun \
    kernel-module-iptable-raw \
    kernel-module-iptable-nat \
    kernel-module-iptable-mangle \
    kernel-module-iptable-filter \
    "

# glibc-utils: for rpcgen
RDEPENDS_packagegroup-poky-vm-sdk = "\
    autoconf \
    automake \
    binutils \
    binutils-symlinks \
    ccache \
    coreutils \
    cpp \
    cpp-symlinks \
    distcc \
    glibc-gconv-ibm850 \
    file \
    findutils \
    g++ \
    g++-symlinks \
    gcc \
    gcc-symlinks \
    intltool \
    ldd \
    less \
    libssp \
    libssp-dev \
    libssp-staticdev \
    libstdc++ \
    libstdc++-dev \
    libtool \
    make \
    mktemp \
    perl-module-re \
    perl-module-text-wrap \
    pkgconfig \
    quilt \
    sed \
    "
# glibc-utils: for rpcgen
RDEPENDS_packagegroup-self-hosted-sdk_append_libc-glibc = "\
    glibc-utils \
    "
RDEPENDS_packagegroup-poky-vm-debug = " \
    gdb \
    gdbserver \
    rsync \
    strace \
    tcf-agent"


RDEPENDS_packagegroup-poky-vm-extended = "\
    bzip2 \
    cifs-utils \
    chkconfig \
    chrpath \
    cpio \
    curl \
    diffstat \
    diffutils \
    elfutils \
    expat \
    gawk \
    gdbm \
    gettext \
    gettext-runtime \
    git \
    git-perltools \
    grep \
    groff \
    gzip \
    settings-daemon \
    libaio \
    libusb1 \
    libxml2 \
    lrzsz \
    lsof \
    lzo \
    mdadm \
    mtools \
    ncurses \
    ncurses-terminfo-base \
    nfs-utils \
    nfs-utils-client \
    openssl \
    openssh-sftp-server \
    opkg \
    opkg-utils \
    patch \
    perl \
    perl-dev \
    perl-modules \
    perl-pod \
    ${PTH} \
    python \
    python-compiler \
    python-git \
    python-misc \
    python-modules \
    python-rpm \
    quota \
    readline \
    rpm \
    samba \
    setserial \
    socat \
    subversion \
    sudo \
    sysstat \
    tar \
    tcl \
    texi2html \
    texinfo \
    unzip \
    usbutils \
    watchdog \
    wget \
    which \
    xinetd \
    zip \
    zlib \
    xz \
    "


RDEPENDS_packagegroup-poky-vm-misc = "\
    libsdl \
    libsdl-dev \
    vmuser \
    "
PTH = "pth"
PTH_libc-uclibc = ""
