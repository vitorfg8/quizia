package com.vitorfg8.quizia.designsystem.icons

import androidx.compose.ui.graphics.vector.ImageVector

private var lightbulbDuotone: ImageVector? = null

internal val LightbulbDuotone: ImageVector
    get() {
        val current: ImageVector? = lightbulbDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Lightbulb",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M208,104a79.86,79.86,0,0,1-30.59,62.92A24.29,24.29,0,0,0,168,186v6a8,8,0,0,1-8,8" +
                        "H96a8,8,0,0,1-8-8v-6a24.11,24.11,0,0,0-9.3-19A79.87,79.87,0,0,1,48,104.45C47.76," +
                        "61.09,82.72,25,126.07,24A80,80,0,0,1,208,104Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M176,232a8,8,0,0,1-8,8H88a8,8,0,0,1,0-16h80A8,8,0,0,1,176,232Zm40-128a87.55,87.5" +
                        "5,0,0,1-33.64,69.21A16.24,16.24,0,0,0,176,186v6a16,16,0,0,1-16,16H96a16,16,0,0,1" +
                        "-16-16v-6a16,16,0,0,0-6.23-12.66A87.59,87.59,0,0,1,40,104.49C39.74,56.83,78.26,1" +
                        "7.14,125.88,16A88,88,0,0,1,216,104Zm-16,0a72,72,0,0,0-73.74-72c-39,.92-70.47,33." +
                        "39-70.26,72.39a71.65,71.65,0,0,0,27.64,56.3A32,32,0,0,1,96,186v6h64v-6a32.15,32." +
                        "15,0,0,1,12.47-25.35A71.65,71.65,0,0,0,200,104Zm-16.11-9.34a57.6,57.6,0,0,0-46.5" +
                        "6-46.55,8,8,0,0,0-2.66,15.78c16.57,2.79,30.63,16.85,33.44,33.45A8,8,0,0,0,176,10" +
                        "4a9,9,0,0,0,1.35-.11A8,8,0,0,0,183.89,94.66Z",
                ),
            ),
        ).also { lightbulbDuotone = it }
    }

private var bankDuotone: ImageVector? = null

internal val BankDuotone: ImageVector
    get() {
        val current: ImageVector? = bankDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Bank",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M232,96H24L128,32Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M24,104H48v64H32a8,8,0,0,0,0,16H224a8,8,0,0,0,0-16H208V104h24a8,8,0,0,0,4.19-14." +
                        "81l-104-64a8,8,0,0,0-8.38,0l-104,64A8,8,0,0,0,24,104Zm40,0H96v64H64Zm80,0v64H112" +
                        "V104Zm48,64H160V104h32ZM128,41.39,203.74,88H52.26ZM248,208a8,8,0,0,1-8,8H16a8,8," +
                        "0,0,1,0-16H240A8,8,0,0,1,248,208Z",
                ),
            ),
        ).also { bankDuotone = it }
    }

private var musicNoteDuotone: ImageVector? = null

internal val MusicNoteDuotone: ImageVector
    get() {
        val current: ImageVector? = musicNoteDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "MusicNote",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M128,184a40,40,0,1,1-40-40A40,40,0,0,1,128,184Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M210.3,56.34l-80-24A8,8,0,0,0,120,40V148.26A48,48,0,1,0,136,184V98.75l69.7,20.91" +
                        "A8,8,0,0,0,216,112V64A8,8,0,0,0,210.3,56.34ZM88,216a32,32,0,1,1,32-32A32,32,0,0," +
                        "1,88,216ZM200,101.25l-64-19.2V50.75L200,70Z",
                ),
            ),
        ).also { musicNoteDuotone = it }
    }

private var filmSlateDuotone: ImageVector? = null

internal val FilmSlateDuotone: ImageVector
    get() {
        val current: ImageVector? = filmSlateDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "FilmSlate",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M67.71,64.59l47.79,27.6L40.43,112,32.27,82a7.76,7.76,0,0,1,5.58-9.52ZM199.84,37." +
                        "76a7.9,7.9,0,0,0-9.66-5.49L126.61,49.05,174.4,76.64,208,67.77Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M216,104H102.09L210,75.51a8,8,0,0,0,5.68-9.84l-8.16-30a15.93,15.93,0,0,0-19.42-1" +
                        "1.13L35.81,64.74a15.75,15.75,0,0,0-9.7,7.4,15.51,15.51,0,0,0-1.55,12L32,111.56c0" +
                        ",.14,0,.29,0,.44v88a16,16,0,0,0,16,16H208a16,16,0,0,0,16-16V112A8,8,0,0,0,216,10" +
                        "4ZM192.16,40l6,22.07-22.62,6L147.42,51.83Zm-66.69,17.6,28.12,16.24-36.94,9.75L88" +
                        ".53,67.37Zm-79.4,44.62-6-22.08,26.5-7L94.69,89.4ZM208,200H48V120H208v80Z",
                ),
            ),
        ).also { filmSlateDuotone = it }
    }

private var soccerBallDuotone: ImageVector? = null

internal val SoccerBallDuotone: ImageVector
    get() {
        val current: ImageVector? = soccerBallDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "SoccerBall",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M65.17,108.08l-33,25.34c-.1-1.8-.15-3.6-.15-5.42A95.61,95.61,0,0,1,53.23,67.78ZM" +
                        "46.92,179.42a96.12,96.12,0,0,0,57,41.52l-14.7-41.52Zm105.21,41.52a96.12,96.12,0," +
                        "0,0,57-41.52H166.83ZM202.77,67.78l-11.94,40.3,33,25.34c.1-1.8.15-3.6.15-5.42A95." +
                        "61,95.61,0,0,0,202.77,67.78Zm-38.52-28.7a96.34,96.34,0,0,0-72.5,0L128,64ZM152.72" +
                        ",160,168,115.5,128,88,88,115.5,103.28,160Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M128,24A104,104,0,1,0,232,128,104.11,104.11,0,0,0,128,24Zm76.52,147.42H170.9l-9." +
                        "26-12.76,12.63-36.78,15-4.89,26.24,20.13A87.38,87.38,0,0,1,204.52,171.42Zm-164-3" +
                        "4.3L66.71,117l15,4.89,12.63,36.78L85.1,171.42H51.48A87.38,87.38,0,0,1,40.47,137." +
                        "12Zm10-50.64,5.51,18.6L40.71,116.77A87.33,87.33,0,0,1,50.43,86.48ZM109,152,97.54" +
                        ",118.65,128,97.71l30.46,20.94L147,152Zm91.07-46.92,5.51-18.6a87.33,87.33,0,0,1,9" +
                        ".72,30.29Zm-6.2-35.38-9.51,32.08-15.07,4.89L136,83.79V68.21l29.09-20A88.58,88.58" +
                        ",0,0,1,193.86,69.7ZM146.07,41.87,128,54.29,109.93,41.87a88.24,88.24,0,0,1,36.14," +
                        "0ZM90.91,48.21l29.09,20V83.79L86.72,106.67l-15.07-4.89L62.14,69.7A88.58,88.58,0," +
                        "0,1,90.91,48.21ZM63.15,187.42H83.52l7.17,20.27A88.4,88.4,0,0,1,63.15,187.42ZM110" +
                        ",214.13,98.12,180.71,107.35,168h41.3l9.23,12.71-11.83,33.42a88,88,0,0,1-36.1,0Zm" +
                        "55.36-6.44,7.17-20.27h20.37A88.4,88.4,0,0,1,165.31,207.69Z",
                ),
            ),
        ).also { soccerBallDuotone = it }
    }

private var planetDuotone: ImageVector? = null

internal val PlanetDuotone: ImageVector
    get() {
        val current: ImageVector? = planetDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Planet",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M216,128a88,88,0,1,1-88-88A88,88,0,0,1,216,128Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M245.11,60.68c-7.65-13.19-27.84-16.16-58.5-8.66A95.93,95.93,0,0,0,32,128a98,98,0" +
                        ",0,0,.78,12.31C5.09,169,5.49,186,10.9,195.32,16,204.16,26.64,208,40.64,208a124.1" +
                        "1,124.11,0,0,0,28.79-4A95.93,95.93,0,0,0,224,128a97.08,97.08,0,0,0-.77-12.25c12." +
                        "5-13,20.82-25.35,23.65-35.92C248.83,72.51,248.24,66.07,245.11,60.68ZM128,48a80.1" +
                        "1,80.11,0,0,1,78,62.2c-17.06,16.06-40.15,32.53-62.07,45.13C116.38,171.14,92.48,1" +
                        "81,73.42,186.4A79.94,79.94,0,0,1,128,48ZM24.74,187.29c-1.46-2.51-.65-7.24,2.22-1" +
                        "3a79.05,79.05,0,0,1,10.29-15.05,96,96,0,0,0,18,31.32C38,193.46,27.24,191.61,24.7" +
                        "4,187.29ZM128,208a79.45,79.45,0,0,1-38.56-9.94,370,370,0,0,0,62.43-28.86c21.58-1" +
                        "2.39,40.68-25.82,56.07-39.08A80.07,80.07,0,0,1,128,208ZM231.42,75.69c-1.7,6.31-6" +
                        ".19,13.53-12.63,21.13a95.69,95.69,0,0,0-18-31.35c14.21-2.35,27.37-2.17,30.5,3.24" +
                        "C232.19,70.28,232.24,72.63,231.42,75.69Z",
                ),
            ),
        ).also { planetDuotone = it }
    }

private var leafDuotone: ImageVector? = null

internal val LeafDuotone: ImageVector
    get() {
        val current: ImageVector? = leafDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Leaf",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M63.81,192.19c-47.89-79.81,16-159.62,151.64-151.64C223.43,176.23,143.62,240.08,6" +
                        "3.81,192.19Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M223.45,40.07a8,8,0,0,0-7.52-7.52C139.8,28.08,78.82,51,52.82,94a87.09,87.09,0,0," +
                        "0-12.76,49c.57,15.92,5.21,32,13.79,47.85l-19.51,19.5a8,8,0,0,0,11.32,11.32l19.5-" +
                        "19.51C81,210.73,97.09,215.37,113,215.94q1.67.06,3.33.06A86.93,86.93,0,0,0,162,20" +
                        "3.18C205,177.18,227.93,116.21,223.45,40.07ZM153.75,189.5c-22.75,13.78-49.68,14-7" +
                        "6.71.77l88.63-88.62a8,8,0,0,0-11.32-11.32L65.73,179c-13.19-27-13-54,.77-76.71,22" +
                        ".09-36.47,74.6-56.44,141.31-54.06C210.2,114.89,190.22,167.41,153.75,189.5Z",
                ),
            ),
        ).also { leafDuotone = it }
    }

private var codeDuotone: ImageVector? = null

internal val CodeDuotone: ImageVector
    get() {
        val current: ImageVector? = codeDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Code",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M240,128l-48,40H64L16,128,64,88H192Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M69.12,94.15,28.5,128l40.62,33.85a8,8,0,1,1-10.24,12.29l-48-40a8,8,0,0,1,0-12.29" +
                        "l48-40a8,8,0,0,1,10.24,12.3Zm176,27.7-48-40a8,8,0,1,0-10.24,12.3L227.5,128l-40.6" +
                        "2,33.85a8,8,0,1,0,10.24,12.29l48-40a8,8,0,0,0,0-12.29ZM162.73,32.48a8,8,0,0,0-10" +
                        ".25,4.79l-64,176a8,8,0,0,0,4.79,10.26A8.14,8.14,0,0,0,96,224a8,8,0,0,0,7.52-5.27" +
                        "l64-176A8,8,0,0,0,162.73,32.48Z",
                ),
            ),
        ).also { codeDuotone = it }
    }

private var gameControllerDuotone: ImageVector? = null

internal val GameControllerDuotone: ImageVector
    get() {
        val current: ImageVector? = gameControllerDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "GameController",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M216.86,207.57a28,28,0,0,1-24.66-7.77L150.09,152H172a51.94,51.94,0,0,0,51.2-61h0" +
                        "l16.36,84.17A28,28,0,0,1,216.86,207.57Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M176,112H152a8,8,0,0,1,0-16h24a8,8,0,0,1,0,16ZM104,96H96V88a8,8,0,0,0-16,0v8H72a" +
                        "8,8,0,0,0,0,16h8v8a8,8,0,0,0,16,0v-8h8a8,8,0,0,0,0-16ZM241.48,200.65a36,36,0,0,1" +
                        "-54.94,4.81c-.12-.12-.24-.24-.35-.37L146.48,160h-37L69.81,205.09l-.35.37A36.08,3" +
                        "6.08,0,0,1,44,216,36,36,0,0,1,8.56,173.75a.68.68,0,0,1,0-.14L24.93,89.52A59.88,5" +
                        "9.88,0,0,1,83.89,40H172a60.08,60.08,0,0,1,59,49.25c0,.06,0,.12,0,.18l16.37,84.17" +
                        "a.68.68,0,0,1,0,.14A35.74,35.74,0,0,1,241.48,200.65ZM172,144a44,44,0,0,0,0-88H83" +
                        ".89A43.9,43.9,0,0,0,40.68,92.37l0,.13L24.3,176.59A20,20,0,0,0,58,194.3l41.92-47." +
                        "59a8,8,0,0,1,6-2.71Zm59.7,32.59-8.74-45A60,60,0,0,1,172,160h-4.2L198,194.31a20.0" +
                        "9,20.09,0,0,0,17.46,5.39,20,20,0,0,0,16.23-23.11Z",
                ),
            ),
        ).also { gameControllerDuotone = it }
    }

private var newspaperDuotone: ImageVector? = null

internal val NewspaperDuotone: ImageVector
    get() {
        val current: ImageVector? = newspaperDuotone
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Newspaper",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M224,64V184a16,16,0,0,1-16,16H32a16,16,0,0,0,16-16V64a8,8,0,0,1,8-8H216A8,8,0,0," +
                        "1,224,64Z",
                    alpha = DUOTONE_SECONDARY_ALPHA,
                ),
                PhosphorPath(
                    data =
                        "M88,112a8,8,0,0,1,8-8h80a8,8,0,0,1,0,16H96A8,8,0,0,1,88,112Zm8,40h80a8,8,0,0,0,0" +
                        "-16H96a8,8,0,0,0,0,16ZM232,64V184a24,24,0,0,1-24,24H32A24,24,0,0,1,8,184.11V88a8" +
                        ",8,0,0,1,16,0v96a8,8,0,0,0,16,0V64A16,16,0,0,1,56,48H216A16,16,0,0,1,232,64Zm-16" +
                        ",0H56V184a23.84,23.84,0,0,1-1.37,8H208a8,8,0,0,0,8-8Z",
                ),
            ),
        ).also { newspaperDuotone = it }
    }
