# Simple Tip Calculator

## *Ying Hang Seah*

**SimpleTip** computes the tip and total amount for a bill.
The app uses the base amount and tip percentage to calculate the total.
The user can adjust the tip percentage and round the total amount.

Time spent: **10+** hours spent in total

## Functionality

The following **required** functionality is completed:

* [x] User can enter in a bill amount (total amount to tip on)
* [x] User can enter a tip percentage (what % the user wants to tip).
* [x] The tip and total amount are updated immediately when any of the inputs changes.
* [x] The user sees a label or color update based on the tip amount.

The following **extensions** are implemented:

* [x] Custom colors palette selected
* [x] Simple and clean design
* [x] Tip adjustment will only show up when the tip section is clicked
* [x] An option to round the total is available when the total is not a whole number

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://i.imgur.com/yzWLIVm.gif' title='Video Walkthrough' height='600' alt='Video Walkthrough' />

GIF created with [EZToGIF](https://ezgif.com/video-to-gif).

## Notes

Creating a clickable section was not easy as it requires creating a view to nest all the elements in the section.
The click event gets blocked when the EditText is clicked and this requires the usage of FocusListener instead.
Lastly, learning how to access / create the right styling was also challenging.

## License

    Copyright [2020] [Ying Hang Seah]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
