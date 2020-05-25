import { Moment } from 'moment';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IFollow {
  id?: number;
  followDate?: string;
  userProfile?: IUserProfile;
  follower?: IUserProfile;
}

export const defaultValue: Readonly<IFollow> = {};
