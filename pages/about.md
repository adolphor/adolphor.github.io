---
layout: page
title: About
description: Keep going
keywords: Bob.Zhu, adolphor
comments: true
menu: 关于
permalink: /about/
---

不走弯路，就是捷径。

如果方向错了，那么努力没有任何意义。

还好，努力，我一直都不缺~

## 联系

<ul>
{% for website in site.data.social %}
    {% if website.sitename contains '邮箱' or website.sitename contains 'mail' %}
        <li>{{website.sitename }}：<a href="mailto:{{ website.url }}">@{{ website.name }}</a></li>
    {% else %}
        <li>{{website.sitename }}：<a href="{{ website.url }}" target="_blank">@{{ website.name }}</a></li>
    {% endif %}
{% endfor %}
{% if site.url contains 'adolphor.github.io' %}
    <li>微信：<br /><img style="height:192px;width:192px;border:1px solid lightgrey;" src="{{ assets_base_url }}/assets/images/bob.zhu.png" alt="Bob.Zhu" /></li>
{% endif %}
</ul>


## Skill Keywords
{% for skill in site.data.skills %}

### {{ skill.name }}
<div class="btn-inline">
    {% for keyword in skill.keywords %}
        <button class="btn btn-outline" type="button">{{ keyword }}</button>
    {% endfor %}
</div>

{% endfor %}
