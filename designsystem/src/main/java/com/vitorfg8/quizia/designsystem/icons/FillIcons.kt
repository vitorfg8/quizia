package com.vitorfg8.quizia.designsystem.icons

import androidx.compose.ui.graphics.vector.ImageVector

private var settingsFill: ImageVector? = null

internal val SettingsFill: ImageVector
    get() {
        val current: ImageVector? = settingsFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Settings",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M237.94,107.21a8,8,0,0,0-3.89-5.4l-29.83-17-.12-33.62a8,8,0,0,0-2.83-6.08,111.91" +
                        ",111.91,0,0,0-36.72-20.67,8,8,0,0,0-6.46.59L128,41.85,97.88,25a8,8,0,0,0-6.47-.6" +
                        "A111.92,111.92,0,0,0,54.73,45.15a8,8,0,0,0-2.83,6.07l-.15,33.65-29.83,17a8,8,0,0" +
                        ",0-3.89,5.4,106.47,106.47,0,0,0,0,41.56,8,8,0,0,0,3.89,5.4l29.83,17,.12,33.63a8," +
                        "8,0,0,0,2.83,6.08,111.91,111.91,0,0,0,36.72,20.67,8,8,0,0,0,6.46-.59L128,214.15," +
                        "158.12,231a7.91,7.91,0,0,0,3.9,1,8.09,8.09,0,0,0,2.57-.42,112.1,112.1,0,0,0,36.6" +
                        "8-20.73,8,8,0,0,0,2.83-6.07l.15-33.65,29.83-17a8,8,0,0,0,3.89-5.4A106.47,106.47," +
                        "0,0,0,237.94,107.21ZM128,168a40,40,0,1,1,40-40A40,40,0,0,1,128,168Z",
                ),
            ),
        ).also { settingsFill = it }
    }

private var checkFill: ImageVector? = null

internal val CheckFill: ImageVector
    get() {
        val current: ImageVector? = checkFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Check",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M243.31,90.91l-128.4,128.4a16,16,0,0,1-22.62,0l-71.62-72a16,16,0,0,1,0-22.61l20-" +
                        "20a16,16,0,0,1,22.58,0L104,144.22l96.76-95.57a16,16,0,0,1,22.59,0l19.95,19.54A16" +
                        ",16,0,0,1,243.31,90.91Z",
                ),
            ),
        ).also { checkFill = it }
    }

private var closeFill: ImageVector? = null

internal val CloseFill: ImageVector
    get() {
        val current: ImageVector? = closeFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Close",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M205.66,194.34a8,8,0,0,1-11.32,11.32L128,139.31,61.66,205.66a8,8,0,0,1-11.32-11." +
                        "32L116.69,128,50.34,61.66A8,8,0,0,1,61.66,50.34L128,116.69l66.34-66.35a8,8,0,0,1" +
                        ",11.32,11.32L139.31,128Z",
                ),
            ),
        ).also { closeFill = it }
    }

private var checkCircleFill: ImageVector? = null

internal val CheckCircleFill: ImageVector
    get() {
        val current: ImageVector? = checkCircleFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "CheckCircle",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M128,24A104,104,0,1,0,232,128,104.11,104.11,0,0,0,128,24Zm45.66,85.66-56,56a8,8," +
                        "0,0,1-11.32,0l-24-24a8,8,0,0,1,11.32-11.32L112,148.69l50.34-50.35a8,8,0,0,1,11.3" +
                        "2,11.32Z",
                ),
            ),
        ).also { checkCircleFill = it }
    }

private var cancelFill: ImageVector? = null

internal val CancelFill: ImageVector
    get() {
        val current: ImageVector? = cancelFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Cancel",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M128,24A104,104,0,1,0,232,128,104.11,104.11,0,0,0,128,24Zm37.66,130.34a8,8,0,0,1" +
                        "-11.32,11.32L128,139.31l-26.34,26.35a8,8,0,0,1-11.32-11.32L116.69,128,90.34,101." +
                        "66a8,8,0,0,1,11.32-11.32L128,116.69l26.34-26.35a8,8,0,0,1,11.32,11.32L139.31,128" +
                        "Z",
                ),
            ),
        ).also { cancelFill = it }
    }

private var accessTimeFill: ImageVector? = null

internal val AccessTimeFill: ImageVector
    get() {
        val current: ImageVector? = accessTimeFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "AccessTime",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M128,24A104,104,0,1,0,232,128,104.11,104.11,0,0,0,128,24Zm56,112H128a8,8,0,0,1-8" +
                        "-8V72a8,8,0,0,1,16,0v48h48a8,8,0,0,1,0,16Z",
                ),
            ),
        ).also { accessTimeFill = it }
    }

private var refreshFill: ImageVector? = null

internal val RefreshFill: ImageVector
    get() {
        val current: ImageVector? = refreshFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Refresh",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M224,48V96a8,8,0,0,1-8,8H168a8,8,0,0,1-5.66-13.66L180.65,72a79.48,79.48,0,0,0-54" +
                        ".72-22.09h-.45A79.52,79.52,0,0,0,69.59,72.71,8,8,0,0,1,58.41,61.27,96,96,0,0,1,1" +
                        "92,60.7l18.36-18.36A8,8,0,0,1,224,48ZM186.41,183.29A80,80,0,0,1,75.35,184l18.31-" +
                        "18.31A8,8,0,0,0,88,152H40a8,8,0,0,0-8,8v48a8,8,0,0,0,13.66,5.66L64,195.3a95.42,9" +
                        "5.42,0,0,0,66,26.76h.53a95.36,95.36,0,0,0,67.07-27.33,8,8,0,0,0-11.18-11.44Z",
                ),
            ),
        ).also { refreshFill = it }
    }

private var homeFill: ImageVector? = null

internal val HomeFill: ImageVector
    get() {
        val current: ImageVector? = homeFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Home",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M224,120v96a8,8,0,0,1-8,8H160a8,8,0,0,1-8-8V164a4,4,0,0,0-4-4H108a4,4,0,0,0-4,4v" +
                        "52a8,8,0,0,1-8,8H40a8,8,0,0,1-8-8V120a16,16,0,0,1,4.69-11.31l80-80a16,16,0,0,1,2" +
                        "2.62,0l80,80A16,16,0,0,1,224,120Z",
                ),
            ),
        ).also { homeFill = it }
    }

private var sparkleFill: ImageVector? = null

internal val SparkleFill: ImageVector
    get() {
        val current: ImageVector? = sparkleFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Sparkle",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M208,144a15.78,15.78,0,0,1-10.42,14.94L146,178l-19,51.62a15.92,15.92,0,0,1-29.88" +
                        ",0L78,178l-51.62-19a15.92,15.92,0,0,1,0-29.88L78,110l19-51.62a15.92,15.92,0,0,1," +
                        "29.88,0L146,110l51.62,19A15.78,15.78,0,0,1,208,144ZM152,48h16V64a8,8,0,0,0,16,0V" +
                        "48h16a8,8,0,0,0,0-16H184V16a8,8,0,0,0-16,0V32H152a8,8,0,0,0,0,16Zm88,32h-8V72a8," +
                        "8,0,0,0-16,0v8h-8a8,8,0,0,0,0,16h8v8a8,8,0,0,0,16,0V96h8a8,8,0,0,0,0-16Z",
                ),
            ),
        ).also { sparkleFill = it }
    }

private var lightbulbFilamentFill: ImageVector? = null

internal val LightbulbFilamentFill: ImageVector
    get() {
        val current: ImageVector? = lightbulbFilamentFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "LightbulbFilament",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M176,232a8,8,0,0,1-8,8H88a8,8,0,0,1,0-16h80A8,8,0,0,1,176,232Zm40-128a87.55,87.5" +
                        "5,0,0,1-33.64,69.21A16.24,16.24,0,0,0,176,186v6a16,16,0,0,1-16,16H96a16,16,0,0,1" +
                        "-16-16v-6a16,16,0,0,0-6.23-12.66A87.59,87.59,0,0,1,40,104.49C39.74,56.83,78.26,1" +
                        "7.14,125.88,16A88,88,0,0,1,216,104Zm-50.34,2.34a8,8,0,0,0-11.32,0L128,132.69l-26" +
                        ".34-26.35a8,8,0,0,0-11.32,11.32L120,147.31V184a8,8,0,0,0,16,0V147.31l29.66-29.65" +
                        "A8,8,0,0,0,165.66,106.34Z",
                ),
            ),
        ).also { lightbulbFilamentFill = it }
    }

private var tuneFill: ImageVector? = null

internal val TuneFill: ImageVector
    get() {
        val current: ImageVector? = tuneFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Tune",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M136,120v96a8,8,0,0,1-16,0V120a8,8,0,0,1,16,0Zm64,72a8,8,0,0,0-8,8v16a8,8,0,0,0," +
                        "16,0V200A8,8,0,0,0,200,192Zm24-48H208V40a8,8,0,0,0-16,0V144H176a8,8,0,0,0-8,8v16" +
                        "a8,8,0,0,0,8,8h48a8,8,0,0,0,8-8V152A8,8,0,0,0,224,144ZM56,160a8,8,0,0,0-8,8v48a8" +
                        ",8,0,0,0,16,0V168A8,8,0,0,0,56,160Zm24-48H64V40a8,8,0,0,0-16,0v72H32a8,8,0,0,0-8" +
                        ",8v16a8,8,0,0,0,8,8H80a8,8,0,0,0,8-8V120A8,8,0,0,0,80,112Zm72-48H136V40a8,8,0,0," +
                        "0-16,0V64H104a8,8,0,0,0-8,8V88a8,8,0,0,0,8,8h48a8,8,0,0,0,8-8V72A8,8,0,0,0,152,6" +
                        "4Z",
                ),
            ),
        ).also { tuneFill = it }
    }

private var keyFill: ImageVector? = null

internal val KeyFill: ImageVector
    get() {
        val current: ImageVector? = keyFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Key",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M216.57,39.43A80,80,0,0,0,83.91,120.78L28.69,176A15.86,15.86,0,0,0,24,187.31V216" +
                        "a16,16,0,0,0,16,16H72a8,8,0,0,0,8-8V208H96a8,8,0,0,0,8-8V184h16a8,8,0,0,0,5.66-2" +
                        ".34l9.56-9.57A79.73,79.73,0,0,0,160,176h.1A80,80,0,0,0,216.57,39.43ZM180,92a16,1" +
                        "6,0,1,1,16-16A16,16,0,0,1,180,92Z",
                ),
            ),
        ).also { keyFill = it }
    }

private var helpOutlineFill: ImageVector? = null

internal val HelpOutlineFill: ImageVector
    get() {
        val current: ImageVector? = helpOutlineFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "HelpOutline",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M128,24A104,104,0,1,0,232,128,104.11,104.11,0,0,0,128,24Zm0,168a12,12,0,1,1,12-1" +
                        "2A12,12,0,0,1,128,192Zm8-48.72V144a8,8,0,0,1-16,0v-8a8,8,0,0,1,8-8c13.23,0,24-9," +
                        "24-20s-10.77-20-24-20-24,9-24,20v4a8,8,0,0,1-16,0v-4c0-19.85,17.94-36,40-36s40,1" +
                        "6.15,40,36C168,125.38,154.24,139.93,136,143.28Z",
                ),
            ),
        ).also { helpOutlineFill = it }
    }

private var paletteFill: ImageVector? = null

internal val PaletteFill: ImageVector
    get() {
        val current: ImageVector? = paletteFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Palette",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M200.77,53.89A103.27,103.27,0,0,0,128,24h-1.07A104,104,0,0,0,24,128c0,43,26.58,7" +
                        "9.06,69.36,94.17A32,32,0,0,0,136,192a16,16,0,0,1,16-16h46.21a31.81,31.81,0,0,0,3" +
                        "1.2-24.88,104.43,104.43,0,0,0,2.59-24A103.28,103.28,0,0,0,200.77,53.89ZM84,168a1" +
                        "2,12,0,1,1,12-12A12,12,0,0,1,84,168Zm0-56a12,12,0,1,1,12-12A12,12,0,0,1,84,112Zm" +
                        "44-24a12,12,0,1,1,12-12A12,12,0,0,1,128,88Zm44,24a12,12,0,1,1,12-12A12,12,0,0,1," +
                        "172,112Z",
                ),
            ),
        ).also { paletteFill = it }
    }

private var visibilityFill: ImageVector? = null

internal val VisibilityFill: ImageVector
    get() {
        val current: ImageVector? = visibilityFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "Visibility",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M247.31,124.76c-.35-.79-8.82-19.58-27.65-38.41C194.57,61.26,162.88,48,128,48S61." +
                        "43,61.26,36.34,86.35C17.51,105.18,9,124,8.69,124.76a8,8,0,0,0,0,6.5c.35.79,8.82," +
                        "19.57,27.65,38.4C61.43,194.74,93.12,208,128,208s66.57-13.26,91.66-38.34c18.83-18" +
                        ".83,27.3-37.61,27.65-38.4A8,8,0,0,0,247.31,124.76ZM128,168a40,40,0,1,1,40-40A40," +
                        "40,0,0,1,128,168Z",
                ),
            ),
        ).also { visibilityFill = it }
    }

private var visibilityOffFill: ImageVector? = null

internal val VisibilityOffFill: ImageVector
    get() {
        val current: ImageVector? = visibilityOffFill
        if (current != null) {
            return current
        }
        return buildPhosphorIcon(
            name = "VisibilityOff",
            paths = listOf(
                PhosphorPath(
                    data =
                        "M96.68,57.87a4,4,0,0,1,2.08-6.6A130.13,130.13,0,0,1,128,48c34.88,0,66.57,13.26,9" +
                        "1.66,38.35,18.83,18.83,27.3,37.62,27.65,38.41a8,8,0,0,1,0,6.5c-.35.79-8.82,19.57" +
                        "-27.65,38.4q-4.28,4.26-8.79,8.07a4,4,0,0,1-5.55-.36ZM213.92,210.62a8,8,0,1,1-11." +
                        "84,10.76L180,197.13A127.21,127.21,0,0,1,128,208c-34.88,0-66.57-13.26-91.66-38.34" +
                        "C17.51,150.83,9,132.05,8.69,131.26a8,8,0,0,1,0-6.5C9,124,17.51,105.18,36.34,86.3" +
                        "5a135,135,0,0,1,25-19.78L42.08,45.38A8,8,0,1,1,53.92,34.62Zm-65.49-48.25-52.69-5" +
                        "8a40,40,0,0,0,52.69,58Z",
                ),
            ),
        ).also { visibilityOffFill = it }
    }
