package com.rongc.feature.ui.host

sealed class Host
open class FragmentHost : Host()
object ActivityHost : Host()
class DialogFragmentHost : FragmentHost()