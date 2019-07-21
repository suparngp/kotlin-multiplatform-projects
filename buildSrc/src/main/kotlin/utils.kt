fun targetName(prefix: String, suffix: String) = "$prefix$suffix"
fun mainTargetName (prefix: String) = targetName(prefix, "Main")
fun testTargetName(prefix: String) = targetName(prefix, "Test")