# SnapMart 运营操作手册

> 覆盖 Facebook Partnership Ads API、落地页代码修改、部署流程、漏斗优化

---

## 一、账户体系

### Business Manager

| BM | ID | 角色 |
|----|-----|------|
| 青岛天泽源盛科技有限公司 | `1522249045620600` | 资产持有方（Page / Pixel / IG 账号） |
| GIMC（省广） | `1614252499057506` | 广告账户持有方（代投） |

两个 BM 的资产需要手动共享才能互通。

### 核心资产

| 资产 | ID | 说明 |
|------|-----|------|
| 广告账户 | `act_646387524897026` | 省广_Partnership_syh_Agentic |
| Facebook Page | `1141388142401039` | Sitin.ai |
| IG Brand 账号 | `17841449380711493` | @sitin.ai_official |
| Pixel | `1562260132108293` | snapmartco |
| API Token（System User） | `122121933962877928` | Conversions API System User，非 BM Admin |
| API 版本 | v22.0 | Base URL: `https://graph.facebook.com/v22.0/` |

### Campaign 结构

```
Campaign: 120248607939040601 (SnapMart Partnership Ads)
  ├── CBO $100/day, OUTCOME_SALES, LOWEST_COST_WITHOUT_CAP
  └── Ad Set: 120248607945680601 (Prospecting - US Female 25-65 Broad)
        ├── 优化目标: PURCHASE
        ├── Pixel: 1562260132108293
        ├── 受众: US, Female, 25-65, Advantage+ Audience
        ├── 归因: 7-day click + 1-day view
        └── Ads: 混合 existing post + dark post 合创广告
```

---

## 二、合创广告（Partnership Ads）API

### 2.1 核心概念：两个身份

一条合创广告有两个身份，都在 adcreative 层设置：

| 身份 | 含义 | 字段 |
|------|------|------|
| 发布身份（identity） | 用户看到"谁发的" | `object_story_spec.page_id` + `.instagram_user_id` |
| 赞助身份（sponsor） | "付费合作伙伴：XXX" | `facebook_branded_content.sponsor_page_id` + `instagram_branded_content.sponsor_id` |

一旦传了 `facebook_branded_content`，`object_story_spec` 里的身份必须是被授权的创作者，不能是品牌方自有身份。

### 2.2 查询 Approved 创作者

```bash
curl --globoff "https://graph.facebook.com/v22.0/17841449380711493/branded_content_ad_permissions\
?fields=creator_username,creator_ig_id,permission_status\
&limit=25\
&access_token=$TOKEN"
```

- `17841449380711493` = 品牌方 IG（Sitin.ai）
- **limit 必须 ≤ 25**，大了会被拒
- 需要翻页取全量（当前 165+ 创作者，7 页）
- 只有 `permission_status=Approved` 才能用
- Approved 不等于能建——部分创作者账号状态异常，需 probe 验证

### 2.3 常用创作者

| Creator | IG ID | 状态 |
|---------|-------|------|
| zhangzifanss | `17841468796981643` | Approved，主力创作者 |
| 2e_b_m5 | `17841400124761414` | Approved |
| __lovelybina | `17841401294772274` | Approved |
| totallyjemdoll | `17841457566720144` | Approved |
| alexasofiasage | `17841478333132018` | Approved |
| victoria_meadows420 | `17841401887405079` | Approved |
| llc.nittygrittycommittee2021 | `17841477734912968` | Approved |
| audi981921 | `17841462990116907` | Approved |

### 2.4 创建 Dark Post 合创广告（自主素材）

Dark post = 不在创作者主页发帖，直接用上传素材创建广告。用 `object_story_spec`。

**第一步：上传图片**

```bash
curl -X POST "https://graph.facebook.com/v22.0/act_646387524897026/adimages" \
  -F "access_token=$TOKEN" \
  -F "filename=@/path/to/image.jpg"
```

返回 `image_hash`，后续创建 creative 时使用。

**第二步：创建 Creative**

```bash
curl -X POST "https://graph.facebook.com/v22.0/act_646387524897026/adcreatives" \
  -F "access_token=$TOKEN" \
  -F "name=Creative - 产品名" \
  -F 'object_story_spec={
    "page_id": "1141388142401039",
    "instagram_user_id": "17841468796981643",
    "link_data": {
      "link": "https://www.snapmartco.com/?pid=product-slug",
      "message": "广告文案...",
      "image_hash": "xxxxx",
      "call_to_action": {
        "type": "SHOP_NOW",
        "value": {"link": "https://www.snapmartco.com/?pid=product-slug"}
      }
    }
  }' \
  -F 'facebook_branded_content={"sponsor_page_id":"1141388142401039"}' \
  -F 'instagram_branded_content={"sponsor_id":"17841449380711493"}'
```

关键点：
- `page_id`：理想情况填创作者 FB Page ID；不知道时回退填品牌方 Page（仅 IG 投放）
- `instagram_user_id`：创作者 IG ID（必须是 Approved 的）
- `facebook_branded_content` 和 `instagram_branded_content` 是**两个独立字段**
- **不要用** `branded_content: {"ad_format": 3}` 这个格式——那是读取时的展示格式，不是写入格式

**第三步：创建 Ad**

```bash
curl -X POST "https://graph.facebook.com/v22.0/act_646387524897026/ads" \
  -F "access_token=$TOKEN" \
  -F "name=Ad X - product-name (Partnership)" \
  -F "adset_id=120248607945680601" \
  -F 'creative={"creative_id":"CREATIVE_ID"}' \
  -F "status=ACTIVE"
```

### 2.5 创建 Existing Post 合创广告（使用现有 IG 帖子）

保留帖子的点赞/评论等社交证据。用 `source_instagram_media_id` + `object_id`。

**CAROUSEL / IMAGE 帖子（一步）**

```bash
curl -X POST "https://graph.facebook.com/v22.0/act_646387524897026/adcreatives" \
  -F "access_token=$TOKEN" \
  -F "name=Creative - 产品名" \
  -F "object_id=1141388142401039" \
  -F "source_instagram_media_id=IG_MEDIA_ID" \
  -F 'call_to_action={"type":"SHOP_NOW","value":{"link":"https://..."}}' \
  -F 'instagram_branded_content={"sponsor_id":"17841449380711493"}' \
  -F 'facebook_branded_content={"sponsor_page_id":"1141388142401039"}' \
  -F 'branded_content={"ad_format":1}'
```

**VIDEO 帖子（两步）**

```bash
# 第一步：上传视频到 FB
curl -X POST "https://graph.facebook.com/v22.0/act_646387524897026/advideos" \
  -F "access_token=$TOKEN" \
  -F "file_url=IG视频的media_url" \
  -F "source_instagram_media_id=IG_MEDIA_ID"
# 返回 video_id，等 status=ready

# 第二步：创建 Creative（同上 CAROUSEL 格式）
```

### 2.6 `source_instagram_media_id` vs `object_story_spec`

| | Existing Post | Dark Post |
|--|---------------|-----------|
| 字段 | `source_instagram_media_id` + `object_id` | `object_story_spec` |
| 社交证据 | 保留（点赞/评论） | 无 |
| 素材 | 用 IG 已有帖子 | 自主上传图片/视频 |
| **互斥** | 两者不能同时使用 | |

### 2.7 修改已有合创广告

**不能直接编辑 creative 的 object_story_spec**（只能改 name/status/adlabels）。

正确做法：
1. 创建新 creative（带正确的创作者身份和 sponsor 标注）
2. 更新 ad 指向新 creative

```bash
# 更新 ad 的 creative
curl -X POST "https://graph.facebook.com/v22.0/{AD_ID}" \
  -F "access_token=$TOKEN" \
  -F 'creative={"creative_id":"NEW_CREATIVE_ID"}'
```

### 2.8 合创创意限制

- **不能用 `asset_feed_spec`**（多标题多正文 + 动态优化），必须用简单的 `link_data` / `video_data`
- 单一 message + 单一标题
- 建号节奏：每两条 ad 之间随机等 5-20 秒，避免风控

### 2.9 查询广告成效

```bash
# 单日成效
curl --globoff "https://graph.facebook.com/v22.0/{CAMPAIGN_ID}/insights\
?fields=spend,impressions,reach,cpm,cpc,ctr,clicks,actions,action_values,purchase_roas,website_ctr\
&time_range={\"since\":\"2026-08-30\",\"until\":\"2026-08-30\"}\
&access_token=$TOKEN"

# 按小时拆分（账户时区 Asia/Shanghai）
# 加 &time_increment=1 按天拆分
# 加 &breakdowns=hourly_stats_aggregated_by_advertiser_time_zone 按小时
```

actions 中的关键事件：
- `landing_page_view` — 落地页浏览
- `view_content` — 浏览内容
- `add_to_cart` — 加购
- `initiate_checkout` — 发起结账
- `add_payment_info` — 添加支付信息
- `purchase` — 购买

### 2.10 常见错误

| 错误 | 原因 | 解决 |
|------|------|------|
| "You don't have required permission to access this profile" | `facebook_branded_content` / `instagram_branded_content` 格式错误，或 token 缺少主页授权 | 用正确的两个独立字段；检查 BM 资产分配 |
| "Instagram Video Must Be Uploaded" | VIDEO 帖子未先上传到 FB | 先 POST /advideos |
| "isn't in compliance with Meta's eligibility requirements" | 创作者账号不合规 | 换一个 Approved 创作者 |
| limit > 25 被拒 | 权限名单 limit 太大 | 降到 25，翻页取全量 |
| "Advantage Audience Flag Required" | v22.0 要求 | Ad Set 加 `targeting_automation.advantage_audience=1` |
| age_max < 65 | Advantage+ 限制 | age_max 设 65 |
| curl 花括号报错 | URL 中 `{}` 被 shell 解析 | 加 `--globoff` |

---

## 三、落地页代码修改

### 3.1 架构

- **SPA 单页应用**：所有内容在一个 `index.html`（~4000 行），包含 HTML + CSS + JS + 产品数据
- **产品数据**：JS 对象数组，每个产品含 `id, name, price, originalPrice, images[], reviews[], description` 等
- **路由**：URL `?pid=product-slug` 定位到具体商品
- **Pixel**：Facebook Pixel `1562260132108293`，追踪 ViewContent / AddToCart / InitiateCheckout / Purchase
- **支付**：Stripe Checkout（服务端 session）

### 3.2 修改流程

```bash
# 1. 解压到临时目录
cd /tmp && rm -rf lp-edit && mkdir lp-edit
cp "/Users/lingzhou/Documents/Project/kotlintest/landing-page.zip" /tmp/
cd lp-edit && unzip /tmp/landing-page.zip

# 2. 编辑 index.html
# （用编辑器或脚本修改）

# 3. 重新打包
cd /tmp/lp-edit && zip -r /tmp/landing-page-new.zip .

# 4. 复制到 kotlintest 并提交
cp /tmp/landing-page-new.zip /Users/lingzhou/Documents/Project/kotlintest/landing-page.zip
cd /Users/lingzhou/Documents/Project/kotlintest
git -c user.name="shenshuo" -c user.email="18500225391@163.com" add landing-page.zip
git -c user.name="shenshuo" -c user.email="18500225391@163.com" commit -m "update landing page"
git push
```

### 3.3 设计 Token（CSS 变量）

```css
:root {
  --primary: #111;
  --accent: #2C2C2C;
  --bg: #FAF8F5;        /* 暖白象牙 */
  --gray: #F2EDE8;
  --border: #E0D8CF;
  --text: #2C2C2C;
  --muted: #8C8279;
  --radius: 8px;
  --gold: #B8976A;       /* 金色点缀 */
}
```

### 3.4 产品数据结构

```javascript
{
  id: 'product-slug',
  name: 'Product Name',
  price: 89.99,
  originalPrice: null,    // 不用划线价
  badge: '',              // 不用 SOLD OUT / LIMITED
  sold: 0,                // 不用虚假销量
  images: ['url1', 'url2'],
  reviews: [
    { author: 'Name', rating: 5, text: '评论内容', date: '2 weeks ago' }
  ],
  description: '...',
  details: ['detail1', 'detail2']
}
```

### 3.5 已做的关键改版（2026-08-30）

| 项目 | 改版前 | 改版后 |
|------|--------|--------|
| 定价 | $3.99-$79.99 | $34-$298 |
| 信任信号 | 倒计时、假库存、夸大 sold、折扣码 | 全部删除 |
| 结账流程 | 先支付后地址 | 先地址后支付 |
| 主题 | 纯白 | 暖白象牙 #FAF8F5 + 金色 #B8976A |
| 品牌定位 | "Designer-inspired" | "Luxury Craftsmanship. Honest Pricing." |
| 评论 | 模板化五星好评 | 52 个商品真人口吻评论，混入 3-4 星 |
| 假通知 | "X just purchased Y" toast | 删除 |
| Lifestyle | 无 | 首页 catalog 下方 5 张多商品合照 |

---

## 四、漏斗分析

### 4.1 漏斗定义

```
展示 (Impressions)
  → 点击 (Clicks / Link Clicks)
    → 落地页浏览 (LPV)
      → 浏览内容 (View Content)
        → 加购 (Add to Cart)
          → 发起结账 (Initiate Checkout)
            → 添加支付信息 (Add Payment Info)
              → 购买 (Purchase)
```

### 4.2 改版前后对比（8/30）

| 环节 | 改版前（8/28-29 均值） | 改版后（8/30） | 行业基准 |
|------|---------------------|---------------|---------|
| CTR | 3.07% | 3.97% | 1-3% |
| LPV→ATC | 1.8% | **13.5%** | 5-8% |
| ATC→IC | 11.1% | 16.7% | 20-40% |
| IC→Purchase | 0% | **50%** | 60-80% |
| ROAS | 0% | **51.3%** | 目标 100%+ |

### 4.3 核心瓶颈

1. **ATC→IC 偏低**（16.7% vs 行业 20-40%）：加购后没有进入结账，可能是结账按钮不够显眼或结账页面跳转有摩擦
2. **样本量太小**：单日 1 笔 purchase 无法判断趋势，需 3-5 天数据
3. **CPM 偏高**（$50-67）：新广告学习期 + 合创广告可能比普通广告 CPM 高
4. **学习期**：大量增删广告会重置学习期，当前应稳定不动

### 4.4 后续计划

- **Phase 1（当前）**：稳定现有广告，积累转化数据
- **Phase 2**：50+ Purchase 后新增 Retargeting Ad Set
- **Phase 3**：学习期结束后切换出价策略为 Minimum ROAS
- **新素材测试**：新建独立 Campaign，不影响现有 Ad Set 学习期

---

## 五、日常查询速查

```bash
TOKEN="..."
ACCT="act_646387524897026"
CAMPAIGN="120248607939040601"
ADSET="120248607945680601"
BRAND_PAGE="1141388142401039"
BRAND_IG="17841449380711493"

# 查看当天成效
curl --globoff "https://graph.facebook.com/v22.0/$CAMPAIGN/insights?fields=spend,impressions,cpm,cpc,ctr,actions,action_values,purchase_roas&time_range={\"since\":\"2026-08-31\",\"until\":\"2026-08-31\"}&access_token=$TOKEN"

# 查看所有 ad 状态
curl --globoff "https://graph.facebook.com/v22.0/$ADSET/ads?fields=id,name,effective_status&limit=50&access_token=$TOKEN"

# 查看 Approved 创作者
curl --globoff "https://graph.facebook.com/v22.0/$BRAND_IG/branded_content_ad_permissions?fields=creator_username,creator_ig_id,permission_status&limit=25&access_token=$TOKEN"

# 上传图片
curl -X POST "https://graph.facebook.com/v22.0/$ACCT/adimages" -F "access_token=$TOKEN" -F "filename=@image.jpg"

# 删除广告
curl -X DELETE "https://graph.facebook.com/v22.0/{AD_ID}?access_token=$TOKEN"

# 删除广告不会丢失历史成效数据
```
