import { Moment } from 'moment';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IPost {
  id?: number;
  postDate?: string;
  postContent?: string;
  userProfile?: IUserProfile;
}

export const defaultValue: Readonly<IPost> = {};
