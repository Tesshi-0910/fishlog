package com.example.fishlog.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 釣果写真のファイル保存・削除だけを担当するサービス。
 * 「どう保存するか」をCatchService/CatchBatchServiceの業務ロジックから切り離すために分けている。
 */
@Service
public class PhotoStorageService {

    private final Path uploadDir;

    public PhotoStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadDir = Path.of(uploadDir);
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new UncheckedIOException("写真保存用ディレクトリの作成に失敗しました: " + this.uploadDir, e);
        }
    }

    /** アップロードされた写真をこのPC上のフォルダに保存し、保存先ファイル名を返す。 */
    public String store(MultipartFile photo) {
        String original = photo.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID() + ext;
        try {
            Files.copy(photo.getInputStream(), uploadDir.resolve(filename));
        } catch (IOException e) {
            throw new UncheckedIOException("写真の保存に失敗しました: " + filename, e);
        }
        return filename;
    }

    /** 保存済みの写真ファイルを削除する。失敗してもアプリ全体は止めず、ログのみ出力する。 */
    public void delete(String filename) {
        try {
            Files.deleteIfExists(uploadDir.resolve(filename));
        } catch (IOException e) {
            System.err.println("写真の削除に失敗しました: " + filename + " (" + e.getMessage() + ")");
        }
    }
}
