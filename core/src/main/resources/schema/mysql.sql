/*
 * MIT License
 *
 * Copyright (c) 2022 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

CREATE TABLE IF NOT EXISTS players (
    uuid            char(36)        NOT NULL,
    username        varchar(16)     NOT NULL,
    lastIP          varchar(45),
    firstJoin       timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lastJoin        timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lastLeave       timestamp,
    lastServer      varchar(32),
    playtime        int             NOT NULL DEFAULT 0,
    totalJoins      int             NOT NULL DEFAULT 1,
    PRIMARY KEY (uuid),
    INDEX (username)
) DEFAULT CHARSET utf8;

CREATE TABLE IF NOT EXISTS bans (
    id              int             AUTO_INCREMENT,
    status          enum('Active', 'Expired', 'Ended') NOT NULL DEFAULT 'Active',
    playerUuid      char(36)        NOT NULL,
    staffUuid       char(36),
    unbanStaffUuid  char(36),
    playerIP        varchar(45),
    banDate         timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unbanDate       timestamp,
    contexts        varchar(255),
    reason          varchar(255),
    unbanReason     varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (playerUuid)        REFERENCES players(uuid),
    FOREIGN KEY (staffUuid)         REFERENCES players(uuid),
    FOREIGN KEY (unbanStaffUuid)    REFERENCES players(uuid),
    INDEX (playerUuid)
) DEFAULT CHARSET utf8mb4;

CREATE TABLE IF NOT EXISTS comments (
    id              int             AUTO_INCREMENT,
    playerUuid      char(36)        NOT NULL,
    staffUuid       char(36),
    dateAdded       timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    isWarning       boolean         NOT NULL DEFAULT 0,
    comment         varchar(255)    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (playerUuid)        REFERENCES players(uuid),
    FOREIGN KEY (staffUuid)         REFERENCES players(uuid),
    INDEX (playerUuid)
) DEFAULT CHARSET utf8mb4;

CREATE TABLE IF NOT EXISTS kicks (
    id              int             AUTO_INCREMENT,
    playerUuid      char(36)        NOT NULL,
    staffUuid       char(36),
    kickDate        timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    isGlobal        boolean         NOT NULL,
    reason          varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (playerUuid)        REFERENCES players(uuid),
    FOREIGN KEY (staffUuid)         REFERENCES players(uuid),
    INDEX (playerUuid)
) DEFAULT CHARSET utf8mb4;

CREATE TABLE IF NOT EXISTS mutes (
    id              int             AUTO_INCREMENT,
    status          enum('Active', 'Expired', 'Ended') NOT NULL DEFAULT 'Active',
    playerUuid      char(36)        NOT NULL,
    staffUuid       char(36),
    unmuteStaffUuid char(36),
    playerIP        varchar(45),
    muteDate        timestamp       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unmuteDate      timestamp,
    contexts        varchar(255),
    reason          varchar(255),
    unmuteReason    varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (playerUuid)        REFERENCES players(uuid),
    FOREIGN KEY (staffUuid)         REFERENCES players(uuid),
    FOREIGN KEY (unmuteStaffUuid)   REFERENCES players(uuid),
    INDEX (playerUuid)
) DEFAULT CHARSET utf8mb4;
