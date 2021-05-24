package com.rongc.feature.ui.host

sealed class Host
object FragmentHost : Host()
object ActivityHost : Host()