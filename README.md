# sas-tok

This summer, Joanne and I created SAS-Tok - an original implementation on SAS' existing CI360 marketing analytical product. As an overview, CI360 retrieves, analyses, and reports data about activity on a company's website: this includes anything from hovering over buttons to clicking on ads to making an account on the website. The primary goal of CI360 is to help SAS' customers make well informed marketing decisions.

Joanne and I wanted to expand the current versions of CI360 by integrating one of our most-used and well-loved social media apps -- TikTok. TikTok is a popular social media app where users can post, interact with, and view short videos. The unique value point of TikTok is their trends - some videos can go viral in 24 hours while others will lose their virality just as fast. Furthermore, this often reflects in buying behavior - many users will be "influenced" to buy a trendy item and the demand can quickly overwhelm the product's supply. We wanted to leverage TikTok's data in trends to meet that consumer demand through CI360's capabilities. 

In this repository, we have included a few files to demonstrate our proposed integration. We used TikAPI, an unofficial TikTok API, to mimic interactions on a company's post. Interactions via liking, commenting, and sharing a post is how a video gets popular and the trend starts to develop with similar videos being made. In particular, we looked into mimicking liking and commenting on the post, and then showing each change being recorded through DataDog, the current metric tracker SAS uses. This logic is shown in the Java file. The Python file shows an earlier iteration of this process, but only sending it to an Excel file to manually record each action. 

In future development, we are able to take these tracked metrics and send them to CI360 to use in current market analysis and future market predictions.

As a disclaimer, this work is intellectual property of SAS and many components of the code are not included due to company policy. 
