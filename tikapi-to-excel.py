import os
import pandas as pd
from tikapi import TikAPI, ValidationException, ResponseException
from datetime import datetime
import openpyxl
from openpyxl.utils.dataframe import dataframe_to_rows

# api key
api = TikAPI('mock_api_key')

# account key associated with user
User = api.user(
    accountKey="mock_account_key"
)


# get video information, return json of video's information
def get_video_info(vid_id):
    try:
        response = api.public.video(
            id=vid_id
        )
        video_info_response = response.json()
        print(video_info_response)
        return video_info_response

    except ValidationException as e:
        print(e, e.field)

    except ResponseException as e:
        print(e, e.response.status_code)


# post a comment
def post_comment(vid_id, text_to_post):
    try:
        response = User.posts.comments.post(
            media_id=vid_id,
            text=text_to_post
        )
        comment_response = response.json()
        print(comment_response)

        # if the comment post was successful, get the video's information
        if comment_response['status_code'] == 0:
            return get_video_info(vid_id)

    except ValidationException as e:
        print(e, e.field)

    except ResponseException as e:
        print(e, e.response.status_code)


# like a post, return json response
def like_post(vid_id):
    try:
        response = User.posts.like(
            media_id=vid_id
        )
        like_response = response.json()
        print(like_response)

        # if the like was successful, get the video's information
        if like_response['status_code'] == 0:
            return get_video_info(vid_id)

    except ValidationException as e:
        print(e, e.field)

    except ResponseException as e:
        print(e, e.response.status_code)


# function to process json response from TikAPI call (subset and add relevant columns)
def process_tiktok_response(response):
    # get current data and time
    now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')

    # normalize json into pandas dataframe
    response_df = pd.json_normalize(response)

    # get information from relevant columns
    subset_df = response_df[['status', 'itemInfo.itemStruct.author.uniqueId', 'itemInfo.itemStruct.desc',
                                'itemInfo.itemStruct.stats.commentCount', 'itemInfo.itemStruct.stats.playCount',
                                 'itemInfo.itemStruct.stats.diggCount', 'itemInfo.itemStruct.stats.shareCount']]

    # add time column
    subset_df['time'] = now
    print(subset_df)
    return subset_df


def append_to_excel(file, sheet_name, df):
    if os.path.isfile(file):  # if file already exists append to existing file
        workbook = openpyxl.load_workbook(file)  # load workbook if already exists
        sheet = workbook[sheet_name]  # declare the active sheet

        # append the dataframe results to the current excel file
        for row in dataframe_to_rows(df, header=False, index=True):
            sheet.append(row)
        workbook.save(file)  # save workbook
        workbook.close()  # close workbook
    else:  # create the excel file if doesn't already exist
        with pd.ExcelWriter(path=file, engine='openpyxl') as writer:
            df.to_excel(writer, index=False, sheet_name=sheet_name)


def main():
    # like a tiktok
    # t1_like = like_post("7251609450732080426")

    # comment on a tiktok
    text = 'I love fireworks'
    t2_comment = post_comment("7252154417535913258", text)

    # format tiktok response to insert into excel
    t2_comment_df = process_tiktok_response(t2_comment)

    filename = r'video_info_data.xlsx'

    # add dataframe to excel sheet
    append_to_excel(filename, 'Sheet3', t2_comment_df)


main()
