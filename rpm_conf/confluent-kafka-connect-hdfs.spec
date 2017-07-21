Name:        confluent-kafka-connect-hdfs
Version:     @@RPM_VERSION@@
Release:     @@RPM_RELEASE@@%{?dist}
Summary:     kafka-connect-hdfs code base
Vendor:      Guavus Network Systems
License:     Proprietary 
URL:         http://www.guavus.com
BuildRoot:   %(mktemp -ud %{_tmppath}/%{name}-%{version}-%{release}-XXXXXX)
Source0:     confluent-kafka-connect-hdfs-%{version}.tar

%define debug_package %{nil}

%define _unpackaged_files_terminate_build 0

%global __os_install_post %{nil}

%global reflex_root_prefix /usr/
%global reflex_root_prefix_etc /etc/

%description
The Reflex Third Party Software Manager.

%prep
%setup -q

%build
# We don't build. We just install.

%install

#
# We cannot do this in the RPM creating script because of assumptions of
# rpmbuild wrt to the Source section.
#
mkdir -p ${RPM_BUILD_ROOT}/%{reflex_root_prefix}
cp -rfP ./target/kafka-connect-hdfs-*-package/share ${RPM_BUILD_ROOT}/%{reflex_root_prefix}
cp -rfP ./target/kafka-connect-hdfs-*-package/etc/ ${RPM_BUILD_ROOT}/%{reflex_root_prefix_etc}

%clean
rm -rf ${buildroot}

%pre

%post
ldconfig

%preun
 
%postun

%files

%attr(-, root, root) /usr/
%attr(-, root, root) /etc/


%changelog


