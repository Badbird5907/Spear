rootProject.name = "Spear"
include("server")
include("client")
include("common")
include("example")
include("example")
include("example:example-server")
findProject(":example:example-server")?.name = "example-server"
include("example:example-client")
findProject(":example:example-client")?.name = "example-client"
include("example:example-common")
findProject(":example:example-common")?.name = "example-common"
