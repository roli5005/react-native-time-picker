#import "TimePickerView.h"
#import "TimePickerSwiftViewWrapper.h"

#import <React/RCTConversions.h>
#import <react/renderer/components/TimePickerViewSpec/ComponentDescriptors.h>
#import <react/renderer/components/TimePickerViewSpec/EventEmitters.h>
#import <react/renderer/components/TimePickerViewSpec/Props.h>
#import <react/renderer/components/TimePickerViewSpec/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"

using namespace facebook::react;

@implementation TimePickerView {
    TimePickerSwiftViewWrapper *_wrapper;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
    return concreteComponentDescriptorProvider<TimePickerViewComponentDescriptor>();
}

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        static const auto defaultProps = std::make_shared<const TimePickerViewProps>();
        _props = defaultProps;
        _wrapper = [[TimePickerSwiftViewWrapper alloc] init];
        self.contentView = _wrapper.view;
    }
    return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
    const auto &p = *std::static_pointer_cast<TimePickerViewProps const>(props);
    [_wrapper setMinuteInterval:p.minuteInterval];
    [_wrapper setLocale:RCTNSStringFromString(p.locale)];
    if (p.value > 0) {
        [_wrapper setValue:p.value];
    }
    if (p.textColor) {
        [_wrapper setTextColor:RCTUIColorFromSharedColor(p.textColor)];
    }
    [super updateProps:props oldProps:oldProps];
}

- (void)updateEventEmitter:(EventEmitter::Shared const &)eventEmitter
{
    [super updateEventEmitter:eventEmitter];

    __weak auto weakSelf = self;
    [_wrapper setOnChange:^(double timestamp) {
        auto strongSelf = weakSelf;
        if (!strongSelf || !strongSelf->_eventEmitter) return;
        auto emitter = std::static_pointer_cast<const TimePickerViewEventEmitter>(strongSelf->_eventEmitter);
        TimePickerViewEventEmitter::OnTimeChange event{};
        event.timestamp = timestamp;
        emitter->onTimeChange(event);
    }];
}

@end
