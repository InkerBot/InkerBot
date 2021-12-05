package com.eloli.inkerbot.core

import java.io.PrintStream

object BannerPrinter {
  private val INKER_BOT_BANNER = arrayOf(
    "    ____      __             ____        __ ",
    "   /  _/___  / /_____  _____/ __ )____  / /_",
    "   / // __ \\/ //_/ _ \\/ ___/ __  / __ \\/ __/",
    " _/ // / / / ,< /  __/ /  / /_/ / /_/ / /_  ",
    "/___/_/ /_/_/|_|\\___/_/  /_____/\\____/\\__/  ",
    "                                            ",
  )

  fun print(out: PrintStream) {
    for (s in INKER_BOT_BANNER) {
      out.println(s)
    }
  }
}