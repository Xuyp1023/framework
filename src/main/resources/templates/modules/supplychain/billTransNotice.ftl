<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN\" \"<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN">
<head>
  <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7,9,10,11" />
  <title>应收账款债权转让通知书</title>
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
    <h3 class="text-align-center">应收账款债权转让通知书</h3>
    <p class="line text-align-right">
      编号：<span class="text-under-line">${(noticeInfo.noticeNo)!''}</span>
    </p>
    <p class="line text-align-left">
      致:<span class="text-under-line">${(noticeInfo.buyer)!''}</span>(买方名称)
    </p>
    <p class="line text-indent">
      根据${(noticeInfo.factorName)!''}(以下简称保理公司)与卖方签订《公开型有追索权国内保理合同》(编号： <span class="text-under-line">${(noticeInfo.factorAgreementNo)!''}</span>)的约定，卖方已将下表所列对贵方的应收账款转让给保理公司，现通知贵方，并请贵方按以下要求支付应收账款。
    </p>
	
	
    <table class="table table-bordered width-100 margin-auto font-14">
      <thead>
        <tr>
          <td width="15%">应收账款转让编号</td>
          <td width="15%">应收账款金额(元)</td>
          <td width="15%">应收账款到期日</td>
          <td width="15%">交易合同编号</td>
          <td width="15%">发票编号</td>
          <td width="15%">发票金额(元)</td>
        </tr>
      </thead>
      
      <tbody>
      	<#assign flag=true>
		<#list creditInfos.creditBillList as creditInfo>
			<#assign billFlag=true>
			<#list creditInfo.billList as bill>
			   <tr>
			   <#if flag>
				 <td rowspan="${creditInfos.total}"  class="text-align-left-im">&nbsp;${(creditInfos.requestNo)!''}</td>
				 <#assign flag=false>	 
			   </#if>
			   <#if billFlag>
				 <td  rowspan="${creditInfo.total}" class="text-align-right-im">&nbsp;￥${(bill.balance)!''}</td>
				 <td  rowspan="${creditInfo.total}" class="text-align-left-im">&nbsp;${(bill.endDate)!''}</td>
				 <td  rowspan="${creditInfo.total}" class="text-align-left-im">&nbsp;${(bill.agreeNo)!''}</td>
				<#assign billFlag=false>	 
			   </#if>
				 <td class="text-align-right-im">&nbsp;${(bill.invoiceNo)!''}</td>
				 <td class="text-align-right-im">&nbsp;￥${(bill.invoiceBalance)!''}</td>
			   </tr>
			</#list>
        </#list>
      </tbody>
    </table>

    <p class="line text-indent">
      1.对于保理公司所受让之账款，保理公司有权按照本通知书及相关的交易合同的规定以自身名义向你方提出及处理索赔，你方应按照本通知书的要求进行支付。
    </p>
    <p class="line text-indent">
      2.保理公司承诺将作为上述商业承兑汇票的最终持有人，不对任何其他方转让该票据。如保理公司转让上述商业承兑汇票，则保理公司丧失该应收账款收款权。
    </p>
    <p class="line text-indent">
      上表所列的各笔应收账款中，如你方已预结算，请确认预结算方式的信息并附上凭证复印件，同时承诺无条件对预结算方式记载事项按时支付。预结算信息如下：
    </p>

    <table class="table table-bordered width-100 margin-auto font-14">
      <thead>
        <tr>
          <td class="text-align-center-im"><strong>结算凭证类型</strong></td>
          <td colspan="2">商业承兑汇票</td>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td width="30%" class="text-align-center-im"><strong>凭证号码</strong></td>
          <td width="30%" class="text-align-center-im">出票金额(小写)</td>
          <td width="40%" class="text-align-center-im">出票金额(大写)</td>
        </tr>
		<#list billMap.billList as bill> 
        <tr>
          <td class="text-align-center-im">${(bill.billNo)!''}</td>
          <td class="text-align-right-im">￥${(bill.balance)!''}</td>
          <td>人民币${(bill.CapitalBalance)!''}</td>
        </tr>
		</#list>
        <tr>
          <td class="text-align-center-im"><strong>总金额</strong></td>
          <td class="text-align-right-im"><strong>￥${(billMap.totalBalance)!''}</strong></td>
          <td><strong>人民币${(billMap.CapitalTotalBalance)!''}</strong></td>
        </tr>
      </tbody>
    </table>

    <p class="line text-indent">
      3.根据前述《国内保理合同》，所有交易合同项下的义务仍由卖方承担，并未转让给保理公司，所有与卖方承担义务有关的诉讼、反诉或反索赔或者抵销，不论是否基于交易合同，均只能向卖方提起，不得向保理公司提起，但在向卖方提出相关争议的同时，应通知保理公司有关争议事项。你方就上表所列任一应收账款发生争议并拒绝按以上第2条承诺向保理公司支付上述任一应收账款的，你方应在有关应收账款相应的到期日前将该等争议事项按本通知书附件《商业纠纷通知书》的格式书面通知保理公司。
    </p>
    <p class="line text-indent">
      4.上表所列的各笔应收账款中，如卖方转让给保理公司的应收账款金额小于你方在应收账款到期日的实际应付金额，你方仍应就差额部分向卖方履行付款义务，该差额部分应通过保理公司向卖方支付。
    </p>
    <p class="line text-indent">
      5.你方按以上第3条发送通知，应寄送或提交至保理公司。
    </p>
    <p class="line">
      地址：<span class="text-under-line">${(noticeInfo.factorAddr)!''}</span>
    </p>
    <p class="line">
      电子邮箱：<span class="text-under-line">${(noticeInfo.email)!''}</span>
    </p>
    <p class="line">
      联系人及联系电话：<span class="text-under-line">${(noticeInfo.factorLinkMan)!''} &nbsp;&nbsp; ${(noticeInfo.phone)!''}</span>
    </p>

    <table class="table width-100 margin-auto margin-top-30 font-14">
      <tbody>
        <tr>
          <td width="40%" class="text-align-left-im">卖方(公章)：<span class="text-under-line">${(noticeInfo.supplierName)!''}</span></td>
          <td width="60%" class="text-align-right-im">保理公司(公章)：${(noticeInfo.factorName)!''}</td>
        </tr>
        <tr>
          <td width="40%" class="text-align-left-im">日期：${(signDate)!''}</td>
          <td width="60%" class="text-align-right-im">日期：	年	月	日</td>
        </tr>
      </tbody>
    </table>

    <p class="line margin-top-30">填写说明: </p>
    <p class="line text-indent">
       1.应收账款金额指保理公司受让的、买方根据《交易合同》在应收账款到期日应向卖方支付的金额，买方在《交易合同》项下的付款义务分期履行的，应分期分别填写应收账款金额;
    </p>
    <p class="line text-indent">
      2.应收账款到期日指《交易合同》所规定的最晚付款日，如果买方在同一《交易合同》项下的付款义务系分期履行的，则该《交易合同》项下应收账款到期日按各期应收账款的最晚付款日分别计算、分别填写;
    </p>
    <p class="line text-indent">
      3.本通知书一式三份，买方、卖方、保理公司各持一份。
    </p>

  </div>
</body>
</html>
