<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN\" \"<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN">
<head>
  <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7,9,10,11" />
  <title>应收账款债权转让通知确认</title>
  <style style="text/css">
    .container{width: 800px;font: normal normal 13px 宋体;padding:40px;}

    .width-100{width: 100%;}
    .width-90{width: 90%;}
    .width-80{width: 80%;}
    .width-85{width: 85%;}
    .width-min-50{min-width: 50%;}

    .margin-auto{margin: auto;}
    .margin-top-30{margin-top: 30px;}

    .text-align-left{text-align: left;}
    .text-align-left-im{text-align: left!important;}
    .text-align-center{text-align: center;}
    .text-align-center-im{text-align: center!important;}
    .text-align-right{text-align: right;}
    .text-align-right-im{text-align: right!important;}
    .text-under-line{text-decoration: underline;}
    .text-indent{text-indent: 2em;}

    .font-14{font-size: 13px;}

    .line{font-size: 13px;font-family: 宋体;line-height: 2em;}
    .line.line-condensed{line-height: 1em;margin: 5px 0;}

    .table{border-collapse: collapse;}
    .table tr td{padding: 12px 6px;text-align: left;vertical-align: middle;}
    .table.table-bordered{border-left: 1px solid black;border-top: 1px solid black;}
    .table.table-bordered tr td{border-bottom: 1px solid black;border-right: 1px solid black;}
  </style>
</head>
<body>
  <div class="container margin-auto">
    <h3 class="text-align-center">应收账款债权转让通知确认</h3>
    <p class="line text-align-right">
      编号：<span class="text-under-line">${(opinionInfo.confirmNo)!''}</span>
    </p>
    <p class="line">
      &nbsp;&nbsp;&nbsp;&nbsp;本公司已收到编号为<span class="text-under-line">${(opinionInfo.noticeNo)!''}</span>的《应收账款转让通知书》，已知晓并确认《应收账款转让通知书》内容，同意按照《应收账款转让通知书》内容执行。
    </p>
    <p class="line line-condensed">我司联系人信息如下：</p>
    <table class="table width-100 margin-auto font-14">
      <tbody>
        <tr>
          <td width="30%">联系人名称：${(opinionInfo.linkName)!''}</td>
          <td width="30%">联系电话：${(opinionInfo.phone)!''}</td>
          <td width="30%">电子邮箱：${(opinionInfo.email)!''}</td>
        </tr>
      </tbody>
    </table>

    <p class="line margin-top-30">特此确认：</p>
    <p class="line text-indent">
       买方（公章）：<span class="text-under-line">${(opinionInfo.buyerName)!''}</span>
    </p>
    <p class="line text-indent">
      日期：${(signDate)!''}
    </p>

  </div>
</body>
</html>
