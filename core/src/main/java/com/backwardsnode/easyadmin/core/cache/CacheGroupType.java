/*
 * MIT License
 *
 * Copyright (c) 2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.core.cache;

import com.backwardsnode.easyadmin.api.data.PunishmentStatus;
import com.backwardsnode.easyadmin.api.record.*;
import com.backwardsnode.easyadmin.core.database.DatabaseController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
public final class CacheGroupType<R, T> {

    public static final CacheGroupType<UUID, BanRecord> BAN_BY_UUID =
            new CacheGroupType<>((db, uuid) -> CacheLoader.collection(db.getPlayerBans(uuid, null)));
    public static final CacheGroupType<String, BanRecord> BAN_BY_ADDR =
            new CacheGroupType<>((db, addr) -> CacheLoader.collection(db.getIPBans(addr, null)));
    public static final CacheGroupType<UUID, BanRecord> ACTIVE_BAN_BY_UUID =
            new CacheGroupType<>((db, uuid) -> CacheLoader.collection(db.getPlayerBansByStatus(uuid, PunishmentStatus.ACTIVE, null)), BAN_BY_UUID);
    public static final CacheGroupType<String, BanRecord> ACTIVE_BAN_BY_ADDR =
            new CacheGroupType<>((db, addr) -> CacheLoader.collection(db.getIPBansByStatus(addr, PunishmentStatus.ACTIVE, null)), BAN_BY_ADDR);
    public static final CacheGroupType<UUID, KickRecord> KICK =
            new CacheGroupType<>((db, uuid) -> CacheLoader.collection(db.getPlayerKicks(uuid, null)));
    public static final CacheGroupType<UUID, MuteRecord> MUTE_BY_UUID =
            new CacheGroupType<>((db, uuid) -> CacheLoader.collection(db.getPlayerMutes(uuid, null)));
    public static final CacheGroupType<String, MuteRecord> MUTE_BY_ADDR =
            new CacheGroupType<>((db, addr) -> CacheLoader.collection(db.getIPMutes(addr, null)));
    public static final CacheGroupType<UUID, MuteRecord> ACTIVE_MUTE_BY_UUID =
            new CacheGroupType<>((db, uuid) -> CacheLoader.collection(db.getPlayerMutesByStatus(uuid, PunishmentStatus.ACTIVE, null)), MUTE_BY_UUID);
    public static final CacheGroupType<String, MuteRecord> ACTIVE_MUTE_BY_ADDR =
            new CacheGroupType<>((db, addr) -> CacheLoader.collection(db.getIPMutesByStatus(addr, PunishmentStatus.ACTIVE, null)), MUTE_BY_ADDR);
    public static final CacheGroupType<UUID, CommentRecord> COMMENT =
            new CacheGroupType<>((db, uuid) -> CacheLoader.collection(db.getPlayerComments(uuid, null)));
    public static final CacheGroupType<UUID, PlayerRecord> PLAYER =
            new CacheGroupType<>((db, uuid) -> CacheLoader.singleton(db.getPlayerRecord(uuid)));


    private final BiFunction<DatabaseController, R, CacheLoader<T>> loader;
    private final CacheGroupType<R, T>[] relatives;

    private CacheGroupType(final BiFunction<DatabaseController, R, CacheLoader<T>> loader, final CacheGroupType<R, T> ...relatives) {
        this.loader = loader;
        this.relatives = relatives;
    }

    CacheLoader<T> retrieve(final DatabaseController db, final R key) {
        return loader.apply(db, key);
    }

    List<CacheGroupType<R, T>> getRelatives() {
        return Arrays.stream(relatives).toList();
    }

}
