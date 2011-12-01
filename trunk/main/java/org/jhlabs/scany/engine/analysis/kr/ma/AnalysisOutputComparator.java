package org.jhlabs.scany.engine.analysis.kr.ma;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Comparator;

public class AnalysisOutputComparator implements Comparator<AnalysisOutput> {
	public int compare(AnalysisOutput o1, AnalysisOutput o2) {
		int score = o2.getScore() - o1.getScore();
		int pattern = o2.getPatn() - o1.getPatn();
		int len = o1.getStem().length() - o2.getStem().length();

		if(score != 0)
			return score;

		if(o2.getScore() == AnalysisOutput.SCORE_CORRECT && o1.getScore() == AnalysisOutput.SCORE_CORRECT) {
			pattern = o1.getPatn() == PatternConstants.PTN_N || o1.getPatn() == PatternConstants.PTN_AID ? -1 : pattern;
			pattern = o2.getPatn() == PatternConstants.PTN_N || o2.getPatn() == PatternConstants.PTN_AID ? 1 : pattern;
		}

		if(pattern != 0)
			return pattern;

		return len;
	}
}
