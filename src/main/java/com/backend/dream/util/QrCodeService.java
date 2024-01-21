package com.backend.dream.util;

import java.io.IOException;

public interface QrCodeService {
    void generateQrCode(String message);

    void deleteQrCodeOlderThanWeek() throws IOException;

    String getQrCode();
}
