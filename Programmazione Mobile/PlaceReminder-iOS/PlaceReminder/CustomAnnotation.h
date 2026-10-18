//
//  CustomAnnotation.h
//  PlaceReminder
//
//  Created by Vincenzo Puca on 08/06/23.
//

#import <MapKit/MapKit.h>
#import "Place.h"

@interface CustomAnnotation : MKPointAnnotation

@property (nonatomic, strong) Place *place;

@end
