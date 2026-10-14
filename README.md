# 釣果管理システム (FishLog)

Spring Boot で構築した、個人の釣果(釣った魚の記録)を管理するポートフォリオ用Webアプリケーションです。
`training-order-system` で用いた技術スタック(Java / Spring Boot / Gradle / Docker)をベースに、
より手軽に動かせるよう DB を H2(ファイル保存)に変更しています。

## 主な機能

- 釣果の記録(魚種・尾数・日時・場所・サイズ・重さ・写真・メモ)
- 1回の釣行で複数魚種をまとめて登録(「魚種を追加」で行を増やせる。日時・場所・写真・メモは共通)
- 一覧表示 / 魚種・場所・期間による検索・絞り込み
- 地図上での釣果ポイント表示(登録フォームの地図クリックで位置を設定。数値入力は不要)
- 漁港・釣り場のマスタ管理(九州地方の有名な漁港を初期データとして登録済み。自分でも追加可能。地図上に釣果とは別の色でピン表示し、レイヤーごとに表示/非表示を切替可能)
- 新規登録時に登録済みの釣り場をプルダウンから選んで位置を自動入力
- 写真アップロード(このPC上のフォルダにファイルとして保存)

## 技術スタック

| 分類 | 技術 |
| --- | --- |
| 言語 / ランタイム | Java 21 |
| フレームワーク | Spring Boot 3.4 (Web, Data JPA, Validation) |
| テンプレートエンジン | Thymeleaf |
| DB(ローカル) | H2 Database(ファイルモード。`./data/fishlog.mv.db` に保存) |
| DB(本番/Render) | PostgreSQL |
| 地図 | Leaflet.js + OpenStreetMap |
| ビルドツール | Gradle |
| デプロイ | Docker / Render |

## クラス構成

役割ごとにクラスを分けており、1つのクラスが複数の責務を持たないようにしている。

```
src/main/java/com/example/fishlog/
├── controller/
│   ├── CatchController        釣果の一覧・まとめ登録・編集・削除
│   ├── FishingSpotController  漁港・釣り場の一覧・登録・編集・削除
│   ├── MapController          地図表示ページ・地図用JSON API(釣果/釣り場)
│   └── HomeController         トップページのリダイレクト
├── service/
│   ├── CatchService            釣果1件ごとの検索・登録・更新・削除
│   ├── CatchBatchService       1回の釣行で複数魚種をまとめて登録する処理
│   ├── FishingSpotService      釣り場の登録・更新・削除・お気に入り追加
│   └── PhotoStorageService     写真ファイルの保存・削除(他のサービスから利用される)
├── repository/   Spring Data JPA リポジトリ(CatchRepository, FishingSpotRepository)
├── entity/       DBエンティティ(Catch, FishingSpot)
├── form/         画面入力フォーム(CatchForm, CatchBatchForm, SpeciesLine, FishingSpotForm)
├── config/       静的リソース設定(WebConfig)、初期データ投入(FishingSpotSeeder)
└── exception/    共通の例外処理
```

単発の釣果登録(`CatchService`)とまとめ登録(`CatchBatchService`)、ページ遷移を返すコントローラーと
地図用のJSON APIを返す`MapController`をあえて分けることで、1クラスの行数と責務を小さく保っている。

## ローカルでの起動方法

DBサーバーのインストールは不要です。リポジトリを取得したら以下のコマンドだけで起動できます。

```bash
./gradlew bootRun
```

起動後、ブラウザで `http://localhost:8080` にアクセスしてください。
データは実行フォルダ内の `./data/fishlog.mv.db`(釣果データ)と `./data/photos/`(写真)に保存されます。
このPC上に残るので、アプリを再起動してもデータは消えません。

## デプロイ (Render)

このリポジトリには [`render.yaml`](./render.yaml) を同梱しており、`training-order-system-portfolio` と同じ手順で
Render の Blueprint 機能を使うと Web サービスと PostgreSQL データベースをまとめて自動構築できます。

1. このフォルダをGitHubリポジトリにpushします。
2. [Render](https://render.com) にサインアップし、GitHubアカウントを連携します。
3. 「New +」→「Blueprint」から、このリポジトリを選択します。
4. `render.yaml` の内容が自動で読み込まれ、Web サービスとDB(PostgreSQL)が作成されます。
5. デプロイ完了後に発行される `https://xxxxx.onrender.com` のURLが、企業に共有できる公開リンクになります。

無料プランのため、しばらくアクセスがないとサービスがスリープし、次回アクセス時に起動まで数十秒かかることがあります。
また無料プランはディスクが永続化されないため、デプロイ環境では再起動時に写真データが消える点にご注意ください
(ローカル実行時はこの制約はありません)。

## 今回あえて削った機能について

`training-order-system` では Spring Security によるログイン機能を実装していますが、
本アプリは「自分一人の釣果を記録する」個人用ツールという要件のため、あえて認証機能を省略しています。
必要であれば同じ構成(Spring Security + BCrypt)を追加することも可能です。
