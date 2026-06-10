package trivy

import data.lib.trivy

default ignore = false

# CNCF Approved Licenses
ignore {
    cncf_allowlist := { "Apache-2.0", "0BSD", "BSD-2-Clause", "BSD-3-Clause", "MIT", "MIT-0", "ISC" }
    input.Name == cncf_allowlist[_]
}

# https://github.com/cncf/foundation/issues/817
ignore {
    cncf_exceptions_817 := { "com.h2database:h2", "com.mysql:mysql-connector-j", "jakarta.annotation:jakarta.annotation-api", "jakarta.el:jakarta.el-api", "jakarta.interceptor:jakarta.interceptor-api", "jakarta.json:jakarta.json-api", "jakarta.resource:jakarta.resource-api", "jakarta.servlet:jakarta.servlet-api", "jakarta.transaction:jakarta.transaction-api", "jakarta.ws.rs:jakarta.ws.rs-api", "javax.xml.bind:jaxb-api", "org.eclipse.parsson:parsson", "org.graalvm.sdk:nativeimage", "org.graalvm.sdk:word", "org.hibernate.common:hibernate-commons-annotations", "org.hibernate.orm:hibernate-core", "org.hibernate.orm:hibernate-graalvm", "org.mariadb.jdbc:mariadb-java-client", "org.openjdk.nashorn:nashorn-core", "org.reactivestreams:reactive-streams" }

    input.PkgName == cncf_exceptions_817[_]
}

# Ignore dual licensed

ignore {
    input.PkgName == "jszip"
    input.Name == "(MIT OR GPL-3.0-or-later)"  
}

ignore {
    input.PkgName == "font-awesome"
    input.Name == "(OFL-1.1 AND MIT)"  
}

ignore {
    input.PkgName == "pako"
    input.Name == "(MIT AND Zlib)"  
}

# Vert.X is dual licensed
ignore {
    startswith(input.PkgName, "io.vertx:vertx")
}
