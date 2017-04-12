/**
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho;

import com.facebook.yoga.YogaAlign;

import com.facebook.yoga.YogaFlexDirection;

import android.util.SparseArray;
import android.view.View;

import com.facebook.litho.testing.ComponentTestHelper;
import com.facebook.litho.testing.testrunner.ComponentsTestRunner;
import com.facebook.litho.testing.TestDrawableComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;

@RunWith(ComponentsTestRunner.class)
public class MountStateViewTagsTest {
  private static final int DUMMY_ID = 0x10000000;

  private ComponentContext mContext;

  @Before
  public void setup() {
    mContext = new ComponentContext(RuntimeEnvironment.application);
  }

  @Test
  public void testInnerComponentHostViewTags() {
    final Object tag1 = new Object();
    final SparseArray<Object> tags1 = new SparseArray<>(1);
    tags1.put(DUMMY_ID, tag1);

    final Object tag2 = new Object();
    final SparseArray<Object> tags2 = new SparseArray<>(1);
    tags2.put(DUMMY_ID, tag2);

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(
                    Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                        .viewTags(tags1)
                        .child(TestDrawableComponent.create(c))
                        .child(TestDrawableComponent.create(c)))
                .child(TestDrawableComponent.create(c))
                .child(
                    TestDrawableComponent.create(c)
                        .withLayout().flexShrink(0)
                        .viewTags(tags2))
                .build();
          }
        });

    final View innerHost1 = componentView.getChildAt(0);
    final View innerHost2 = componentView.getChildAt(1);

    assertEquals(tag1, innerHost1.getTag(DUMMY_ID));
    assertEquals(tag2, innerHost2.getTag(DUMMY_ID));
  }

  @Test
  public void testRootHostViewTags() {
    final Object tag = new Object();
    final SparseArray<Object> tags = new SparseArray<>(1);
    tags.put(DUMMY_ID, tag);

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Column.create(c).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .viewTags(tags)
                .child(TestDrawableComponent.create(c))
                .child(TestDrawableComponent.create(c))
                .build();
          }
        });

    assertEquals(tag, componentView.getTag(DUMMY_ID));
  }
}