#!/usr/bin/env python3
"""百听听书批量下载脚本"""

import json
import os
import re
import sys
import time
import urllib.request
import urllib.error

TOKEN = "Token 274758c40d59a056959747d792bec52b1aad6b8f0a4d2fedf597a056e35bc3d9"
DEVICE_UUID = "59f4b0c1-9564-46c3-9eda-f791983c816d"
BASE = "https://www.bookting.cn/api/v2"
OUTPUT_DIR = os.path.expanduser("~/Documents/Project/bookting_audio")

HEADERS = {
    "accept": "application/json",
    "app-bundle": "com.longrundmt.bookting-web",
    "app-version": "2025.3.5",
    "authorization": TOKEN,
    "device-uuid": DEVICE_UUID,
    "user-agent": "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Mobile Safari/537.36",
    "x-requested-with": "XMLHttpRequest",
}

BUNDLES = {
    "3d45d4a005b0": "鹿鼎记",
    "68d5e8d8e5e4": "倚天屠龙记",
    "3d45d349b5b0": "神雕侠侣",
}


def api_get(path):
    url = f"{BASE}/{path}"
    req = urllib.request.Request(url, headers=HEADERS)
    with urllib.request.urlopen(req, timeout=30) as resp:
        return json.loads(resp.read())


def safe_filename(name):
    return re.sub(r'[\\/:*?"<>|]', '_', name).strip()


def download_file(url, filepath):
    req = urllib.request.Request(url, headers={"User-Agent": HEADERS["user-agent"]})
    with urllib.request.urlopen(req, timeout=120) as resp:
        total = int(resp.headers.get("Content-Length", 0))
        downloaded = 0
        with open(filepath, "wb") as f:
            while True:
                chunk = resp.read(1024 * 64)
                if not chunk:
                    break
                f.write(chunk)
                downloaded += len(chunk)
                if total > 0:
                    pct = downloaded * 100 // total
                    mb = downloaded / 1024 / 1024
                    print(f"\r  下载中: {mb:.1f}MB ({pct}%)", end="", flush=True)
    print()


def main():
    os.makedirs(OUTPUT_DIR, exist_ok=True)

    for bundle_id, bundle_name in BUNDLES.items():
        print(f"\n{'='*60}")
        print(f"📚 {bundle_name} (bundle: {bundle_id})")
        print(f"{'='*60}")

        bundle_data = api_get(f"books/{bundle_id}")
        sub_books = bundle_data.get("books", [])

        if not sub_books:
            print(f"  没有子专辑，跳过")
            continue

        print(f"  共 {len(sub_books)} 个子专辑")

        for book_idx, book in enumerate(sub_books):
            book_id = book["id"]
            book_title = book.get("title", book.get("name", f"专辑{book_idx+1}"))
            book_dir = os.path.join(OUTPUT_DIR, safe_filename(bundle_name), safe_filename(book_title))
            os.makedirs(book_dir, exist_ok=True)

            print(f"\n  📖 [{book_idx+1}/{len(sub_books)}] {book_title}")

            sections = api_get(f"sections?num=9999&id={book_id}")
            print(f"     共 {len(sections)} 集")

            for sec_idx, item in enumerate(sections):
                sec = item["section"]
                sec_id = sec["id"]
                sec_title = sec["title"]
                sec_num = sec.get("section_number", sec_idx)

                filename = f"{sec_num:03d}_{safe_filename(sec_title)}.mp3"
                filepath = os.path.join(book_dir, filename)

                if os.path.exists(filepath) and os.path.getsize(filepath) > 1024:
                    print(f"  ✓ 已存在，跳过: {filename}")
                    continue

                print(f"  [{sec_idx+1}/{len(sections)}] {sec_title}")

                try:
                    play_data = api_get(f"play?id={sec_id}&type=section")
                except urllib.error.HTTPError as e:
                    print(f"    ✗ 获取播放地址失败: {e}")
                    continue

                if "url" not in play_data:
                    msg = play_data.get("msg", play_data)
                    print(f"    ✗ 无法播放: {msg}")
                    continue

                audio_url = play_data["url"]

                try:
                    download_file(audio_url, filepath)
                    print(f"    ✓ {filename}")
                except Exception as e:
                    print(f"    ✗ 下载失败: {e}")
                    if os.path.exists(filepath):
                        os.remove(filepath)

                time.sleep(0.3)

    print(f"\n\n下载完成！文件保存在: {OUTPUT_DIR}")


if __name__ == "__main__":
    main()
