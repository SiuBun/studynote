/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wsb.customview.trans;

import androidx.annotation.DrawableRes;

import com.wsb.customview.R;

/**
 * Holds the image resource references used by the grid and the pager fragments.
 */
abstract class ImageData {

  // Image assets (free for commercial use, no attribution required, from pixabay.com)
  @DrawableRes
  static final int[] IMAGE_DRAWABLES = {
          R.drawable.floating_account,
          R.drawable.floating_bulletin,
          R.drawable.floating_community,
          R.drawable.floating_customer,
          R.drawable.floating_hide,
          R.drawable.floating_logo,
          R.drawable.floating_msg,
          R.drawable.what_the_fuck,
          R.drawable.ic_launcher_foreground,
  };

}