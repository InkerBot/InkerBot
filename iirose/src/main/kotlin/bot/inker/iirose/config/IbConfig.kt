package bot.inker.iirose.config

class IbConfig {
  @bot.inker.api.config.KotlinComment(
    "IIROSE的ws地址",
    "测试环境: wss://m0.iirose.com:8778/ (已关闭）",
    "真实环境: wss://m1.iirose.com:8778/"
  )
  var wsUrl: String = "wss://m1.iirose.com:8778/"

  @bot.inker.api.config.KotlinComment(
    "房间ID",
    "格式类似于 5fb7c5c3e3413",
    "获取方式：房间->目录->菜单->房间地址"
  )
  var room: String = "5fb7c5c3e3413"

  @bot.inker.api.config.Comment("机器人的用户名")
  var username: String = "logos"

  @bot.inker.api.config.Comment("机器人的密码")
  var password: String = ""
}