//
//  Place.h
//  PlaceReminder
//
//  Created by Vincenzo Puca on 07/06/23.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

@interface Place : NSObject <NSCoding>

@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSString *customName;
@property (nonatomic, assign) CLLocationDegrees latitude;
@property (nonatomic, assign) CLLocationDegrees longitude;
@property (nonatomic, strong) NSString *descriptionText;
@property (nonatomic, strong) NSDate *dateAdded;

- (instancetype)initWithName:(NSString *)name latitude:(CLLocationDegrees)latitude longitude:(CLLocationDegrees)longitude description:(NSString *)descriptionText dateAdded:(NSDate *)dateAdded customName:(NSString *)customName;

@end
